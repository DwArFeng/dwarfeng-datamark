# Handler Reference - 处理器 API 参考

## 综述

本文档集中说明 `dwarfeng-datamark` 处理器 API 的调用语义，
包括数据标记值的读取、刷新、更新、缓存、并发访问、QoS 路由与异常处理。

处理器 API 位于 `dwarfeng-datamark-core` 模块。
完成 Spring 配置后，可以直接注入 `DatamarkHandler`；
如果应用上下文中存在多个处理器，则可以通过 `DatamarkQosService` 按处理器名称统一访问。

如果您尚未完成基础配置，请先阅读 [Quick Start](./QuickStart.md)。
配置参数、资源类型与读写限制分别参阅 [Config Parameters](./ConfigParameters.md) 和
[Resource Support](./ResourceSupport.md)。

## DatamarkHandler 接口

`DatamarkHandler` 表示一个数据标记处理器。每个处理器对应一个 Spring Bean，并通过配置关联一个 Spring `Resource`。
接口提供以下四个操作：

```java
boolean updateAllowed();
String get();
String refresh();
String update(String datamarkValue);
```

接口方法声明抛出 `HandlerException`。具体异常会根据调用阶段表示更新策略、资源读写或数据标记值校验失败。

### updateAllowed()

`updateAllowed()` 返回处理器配置中的更新开关，不读取资源，也不改变缓存。

```java
boolean allowed = datamarkHandler.updateAllowed();
```

返回值为 `true` 时，处理器具备执行更新操作的配置前提；返回值为 `false` 时，
调用 `update(...)` 会抛出 `UpdateNotAllowedException`。该方法只反映当前处理器配置，不代表资源本身一定可写。

### get()

`get()` 返回当前数据标记值。处理器会先检查缓存：

- 缓存存在时，直接返回缓存中的值。
- 缓存尚未初始化时，从配置的资源中读取数据并初始化缓存。
- 读取成功后，返回经过校验的数据标记值。

```java
String value = datamarkHandler.get();
```

资源发生外部变化后，已经初始化的缓存不会被 `get()` 自动替换。此时需要调用 `refresh()` 重新读取资源。

### refresh()

`refresh()` 强制从资源读取数据，并使用读取结果替换当前缓存。

```java
String refreshedValue = datamarkHandler.refresh();
```

刷新只读取资源的第一行。读取内容会按照 `resource-charset` 指定的字符集解析，并在写入缓存前执行数据标记值校验。
读取失败或读取到非法值时，刷新不会返回旧值：读取失败和校验失败都会将缓存置为空，然后抛出相应异常。

### update(String)

`update(String)` 将新的数据标记值写入资源，并在写入成功后同步更新缓存。

```java
String updatedValue = datamarkHandler.update("release-2026.09.05");
```

更新过程依次执行以下检查和操作：

1. 检查处理器是否允许更新。
2. 检查新的数据标记值是否合法。
3. 获取资源并检查资源是否实现 `WritableResource`。
4. 使用配置字符集写入数据标记值。
5. 写入成功后，将新的值放入缓存并返回。

写入内容以一行文本形式写入资源。更新权限关闭、值非法、资源不可写或资源写入失败时，缓存不会因为本次失败更新为新值。

## 缓存与并发行为

处理器为每个实例维护一个数据标记值缓存。缓存只在成功读取并通过校验后初始化，或在成功写入后更新。

实现使用读写锁保护缓存与资源操作：读取已存在的缓存使用读锁；首次读取、刷新和更新使用写锁。
因此，同一个处理器实例的刷新与更新不会与另一个刷新或更新操作并行执行，资源读取和写入过程也不会交叉进行。

这里的缓存是处理器实例级缓存，不是资源级缓存。多个处理器即使配置了相同的资源 URL，也分别维护自己的缓存。

### 首次读取

首次调用 `get()` 时，如果缓存尚未初始化，处理器会从 `DatamarkConfig.resourceUrl` 获取资源，
使用 `DatamarkConfig.resourceCharset` 创建读取器，并检查资源是否包含数据行。

资源存在数据行时，只读取第一行作为临时数据标记值。读取完成后，临时值必须通过合法性校验，校验成功后才会写入缓存。

资源没有数据行时，临时值为空字符串。
空字符串不满足数据标记值合法性要求，因此首次 `get()` 会抛出 `IllegalDatamarkValueException`，缓存保持为空。

### 刷新流程

`refresh()` 不使用当前缓存作为返回结果，而是直接执行资源读取流程。资源读取成功且第一行合法时，缓存被替换为新值。

如果刷新阶段发生资源读取异常，处理器会清空缓存并抛出 `ResourceReadFailedException`。
如果资源读取成功但第一行不合法，处理器会清空缓存并抛出 `IllegalDatamarkValueException`。
因此，刷新失败后，下一次 `get()` 仍会尝试重新读取资源，而不会继续返回失效缓存。

### 更新流程

`update(...)` 在写入成功前不会修改缓存。
更新值通过权限、格式和资源类型检查后，处理器向 `WritableResource` 获取输出流，并使用配置字符集写入一行数据。

资源写入成功后，缓存立即替换为更新值。
资源写入失败时，缓存保留更新前的状态，并抛出 `ResourceWriteFailedException`。
调用方不应在捕获写入异常后假定资源或缓存已经包含新值。

## 数据标记值校验

处理器使用 `DatamarkValueUtil.isDatamarkValueValid(...)` 校验从资源读取的值以及通过 `update(...)` 传入的值。
校验规则同时适用于读取和更新，避免资源中的无效内容进入缓存。

### 合法值要求

数据标记值需要满足以下条件：

- 不能为 `null`。
- 不能是空字符串。
- 长度不能超过 `100` 个字符。
- 首字符和末字符不能是空白字符。
- 中间可以包含普通空格，但不能包含制表符等其他空白字符。

例如，以下值合法：

```text
a
alpha beta
release-2026.09.05
```

以下值非法：

```text

 alpha
alpha 
alpha	beta
```

传入非法值时，`update(...)` 或 `refresh()` 会抛出 `IllegalDatamarkValueException`，调用方应修正值或资源内容后重新执行操作。

### 空资源处理

资源为空文件或不包含任何数据行时，处理器得到的临时值是空字符串。空字符串不会作为有效的“无值”进入缓存，而是按非法数据标记值处理。

因此，资源至少应包含一行符合校验规则的数据。需要表达特殊状态时，应使用明确的非空标记值，而不是依赖空文件或空字符串。

### 首行读取规则

读取操作只消费资源的第一行，后续行不会参与数据标记值解析。
资源文件中存在多行内容时，第一行必须是有效数据标记值；后续内容不会成为 `get()` 或 `refresh()` 的返回值。

读取和写入均使用 `resource-charset` 指定的字符集。
中文或其他非 ASCII 内容应确保资源实际编码与配置保持一致，通常建议显式配置为 `UTF-8`。

## DatamarkQosService

`DatamarkQosService` 为一个或多个 `DatamarkHandler` 提供统一的服务入口。
它除了转发处理器操作，还提供 `listHandlerNames()` 用于查看当前上下文中的处理器名称。

```java
List<String> handlerNames = datamarkQosService.listHandlerNames();
boolean allowed = datamarkQosService.updateAllowed(handlerName);
String value = datamarkQosService.get(handlerName);
String refreshedValue = datamarkQosService.refresh(handlerName);
String updatedValue = datamarkQosService.update(handlerName, datamarkValue);
```

QoS 服务将底层处理器异常转换为 `ServiceException`。
使用服务层的调用方应按服务异常处理约定记录或向上层传递异常，不应直接依赖内部处理器实现类。

### 单处理器调用

当应用上下文中只有一个 `DatamarkHandler` 时，`handlerName` 可以传入 `null`，QoS 处理器会自动选择唯一处理器。

```java
String value = datamarkQosService.get(null);
```

也可以显式传入该处理器的 Bean 名称。显式传名有助于让业务代码在单例配置迁移到多例配置时保持调用形式稳定。

### 多处理器调用

当应用上下文中存在多个 `DatamarkHandler` 时，调用方必须传入目标处理器的 Bean 名称：

```java
String value = datamarkQosService.get("foobarNodeDatamarkHandler");
```

多处理器场景下将 `handlerName` 传入 `null` 会抛出 `AmbiguousDatamarkHandlerException`。
名称不存在时会抛出 `DatamarkHandlerNotFoundException`。处理器名称可以通过 `listHandlerNames()` 查看，返回列表按名称排序。

### handlerName 解析

`handlerName` 对应 Spring 容器中的 `DatamarkHandler` Bean 名称，不是资源 URL、配置前缀或业务数据标记值。

使用 XML 命名空间配置时，`datamark:handler` 的 `handler-name` 属性决定处理器 Bean 名称。例如：

```xml
<datamark:handler
        handler-name="foobarNodeDatamarkHandler"
        resource-url="file:conf/datamark/current-foobar-node.storage"
        resource-charset="UTF-8"
        update-allowed="true"
/>
```

QoS 调用中的 `handlerName` 必须与 `handler-name` 完全一致，包括大小写。
有关处理器注册方式，参阅 [Usage Guide](./UsageGuide.md) 的配置与单例、多例集成章节。

## 异常处理

直接调用 `DatamarkHandler` 时，底层异常以 `HandlerException` 体系抛出；
通过 `DatamarkQosService` 调用时，底层异常会被包装为 `ServiceException`。处理异常时应结合调用阶段检查配置、资源状态和传入值。

### UpdateNotAllowedException

`update(...)` 在 `update-allowed` 为 `false` 时抛出此异常。该异常表示更新策略禁止写入，并不表示资源权限或文件系统状态异常。

请检查对应处理器的 `update-allowed` 配置。只读资源通常应明确关闭更新权限，避免调用方误将策略限制判断为资源故障。

### ResourceNotWritableException

当更新权限已开启，但通过 `resource-url` 获取到的资源没有实现 Spring `WritableResource` 时，处理器抛出此异常。

常见情况是使用 `classpath:` 资源作为更新目标。
请改用适合写入的资源类型，例如具有写权限的 `file:` 资源，并确认配置指向的是实际更新目标。

### ResourceReadFailedException

处理器在首次读取或刷新时无法打开资源、读取资源或按指定字符集解析资源，会抛出此异常。

诊断时应检查 `resource-url` 是否正确、资源是否存在、应用进程是否具有读取权限，
以及 `resource-charset` 是否为有效字符集名称。刷新读取失败后缓存会被清空。

### ResourceWriteFailedException

处理器在资源类型检查通过后，获取输出流或写入数据失败时抛出此异常。

诊断时应检查目标路径、父目录、文件权限、文件系统空间以及资源是否仍然可写。
写入失败不会将新值同步到缓存，调用方应重新确认资源内容和当前缓存状态。

### IllegalDatamarkValueException

资源第一行或 `update(...)` 参数不满足数据标记值校验规则时，处理器抛出此异常。

请检查值是否为空、是否超过 `100` 个字符、首尾是否包含空白字符，以及是否包含制表符等不允许的空白字符。
读取阶段出现该异常时，缓存不会保留非法值。

## 最佳实践

### 缓存使用

- 对同一处理器的普通读取使用 `get()`，避免在每次读取时都强制访问资源。
- 只有在资源可能被外部修改，或需要主动确认资源最新内容时调用 `refresh()`。
- 不要将 `get()` 当作外部资源实时监视接口；缓存更新需要显式刷新或通过本处理器成功更新。

### 字符集与资源

- 显式配置 `resource-charset`，并确保资源文件实际编码一致。
- 读取资源与更新资源应使用同一字符集，中文场景通常使用 `UTF-8`。
- 需要更新时优先选择明确支持写入的文件系统资源，并提前验证应用进程的读写权限。

### 更新权限

- 将 `update-allowed=false` 用于发布产物或远程只读资源等不应被应用修改的场景。
- 需要动态调整标记值时，开启更新权限并为目标资源设置最小必要的文件系统权限。
- 将更新操作纳入审计或运维日志，记录处理器名称、原值、新值和操作来源。

### 多处理器路由

- 多处理器场景始终显式传入 `handlerName`，不要依赖 `null` 路由。
- 将处理器名称与业务数据域保持一一对应，避免多个数据域共用一个资源。
- 可以在启动检查或运维检查中调用 `listHandlerNames()`，确认处理器 Bean 已按预期注册。

## 参阅

- [Quick Start](./QuickStart.md) - 快速体验处理器的读取、刷新与更新能力。
- [Config Parameters](./ConfigParameters.md) - 了解资源 URL、字符集和更新开关等配置参数。
- [Resource Support](./ResourceSupport.md) - 了解 Spring 资源类型以及资源读写限制。
- [Usage Guide](./UsageGuide.md) - 了解单例、多例、XML 配置和 Telqos 运维方式。
