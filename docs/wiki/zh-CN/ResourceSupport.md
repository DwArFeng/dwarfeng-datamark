# Resource Support - 资源支持

dwarfeng-datamark 项目支持多种类型的资源，资源的解析完全委托给 Spring 框架处理，因此支持所有 Spring 框架能够解析的资源类型。

## 默认配置

虽然默认配置中使用的是 classpath 资源，但项目支持更多类型的资源，特别是文件类型的资源，不仅支持读取，还支持更新操作。

默认配置如下：

```java
import com.dwarfeng.datamark.struct.DatamarkConfig;
import com.dwarfeng.dutil.basic.prog.Buildable;

import java.nio.charset.Charset;

public static final class Builder implements Buildable<DatamarkConfig> {

    // 默认资源 URL，使用 classpath 资源。
    public static final String DEFAULT_RESOURCE_URL = "classpath:datamark/default.storage";
    public static final String DEFAULT_RESOURCE_CHARSET = Charset.defaultCharset().name();
    public static final boolean DEFAULT_UPDATE_ALLOWED = false;

    // ...
}
```

## 支持的资源类型

### Classpath 资源

**协议前缀**: `classpath:`

**说明**: 从类路径中加载资源，通常用于打包在 JAR 文件中的资源。

**示例**:

```properties
datamark.resource_url=classpath:datamark/default.storage
```

**特点**:

- 支持读取。
- 不支持写入（classpath 资源通常是只读的）。
- 适合打包在应用中的默认配置。

### 文件系统资源

**协议前缀**: `file:`

**说明**: 从文件系统中加载资源，支持绝对路径和相对路径。

**示例**:

```properties
# 绝对路径示例。
datamark.resource_url=file:/path/to/your/datamark.storage
```

```properties
# 相对路径示例。
datamark.resource_url=file:conf/test.datamark/singleton/current.storage
```

**特点**:

- 支持读取。
- 支持写入（当文件可写时）。
- 适合运行时动态更新的场景。

### HTTP/HTTPS 资源

**协议前缀**: `http:` 或 `https:`

**说明**: 从网络 URL 加载资源。

**示例**:

```properties
datamark.resource_url=https://example.com/api/datamark
```

**特点**:

- 支持读取。
- 不支持写入（HTTP 资源通常是只读的）。
- 适合从远程服务获取数据标记。

### 其他 Spring 支持的资源类型

项目支持所有 Spring 框架能够解析的资源类型，包括但不限于：

- **FTP 资源**: `ftp://`。
- **JAR 资源**: `jar:`。
- **ZIP 资源**: `zip:`。
- **自定义 ResourceLoader**: 通过 Spring 的 ResourceLoader 机制。

## 配置示例

### 单例模式配置

```properties
# 数据标记资源的 URL。
datamark.resource_url=file:conf/test.datamark/singleton/current.storage
# 数据标记资源的字符集。
datamark.resource_charset=UTF-8
# 数据标记是否允许更新。
datamark.update_allowed=true
```

### 多例模式配置

```properties
# Instance1 配置。
datamark.instance1.resource_url=file:conf/test.datamark/multiton/current-instance1.storage
datamark.instance1.resource_charset=UTF-8
datamark.instance1.update_allowed=true
# Instance2 配置。
datamark.instance2.resource_url=file:conf/test.datamark/multiton/current-instance2.storage
datamark.instance2.resource_charset=UTF-8
datamark.instance2.update_allowed=true
# Instance3 配置。
datamark.instance3.resource_url=file:conf/test.datamark/multiton/current-instance3.storage
datamark.instance3.resource_charset=UTF-8
datamark.instance3.update_allowed=true
```

## 读写权限说明

### 读取权限

所有 Spring Resource 都支持读取操作，项目通过以下方式读取数据：

```java
import com.dwarfeng.datamark.struct.DatamarkConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;

import java.io.InputStream;
import java.util.Scanner;

public class DatamarkReader {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private ApplicationContext ctx;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private DatamarkConfig datamarkConfig;

    @SuppressWarnings("EmptyTryBlock")
    public void readDatamarkValue() {
        Resource resource = ctx.getResource(datamarkConfig.getResourceUrl());
        try (InputStream in = resource.getInputStream();
             Scanner scanner = new Scanner(in, datamarkConfig.getResourceCharset())) {
            // 读取数据标记值
        }
    }
}
```

### 写入权限

只有实现了 `WritableResource` 接口的资源才支持写入操作。项目会进行以下检查：

```java
import org.springframework.core.io.Resource;
import org.springframework.core.io.WritableResource;
import com.dwarfeng.datamark.struct.DatamarkConfig;
import com.dwarfeng.datamark.exception.ResourceNotWritableException;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;

public class DatamarkWriter {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private ApplicationContext ctx;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private DatamarkConfig datamarkConfig;

    public void checkWritableResource() {
        Resource resource = ctx.getResource(datamarkConfig.getResourceUrl());
        if (!(resource instanceof WritableResource)) {
            throw new ResourceNotWritableException(datamarkConfig.getResourceUrl());
        }
    }
}
```

**支持写入的资源类型**:

- 文件系统资源 (`file:`)。
- 其他实现了 `WritableResource` 接口的自定义资源。

**不支持写入的资源类型**:

- Classpath 资源 (`classpath:`)。
- HTTP/HTTPS 资源 (`http:`, `https:`)。
- 其他只读资源。

## 异常处理

项目定义了专门的异常类来处理资源相关的错误：

### ResourceReadFailedException

当资源读取失败时抛出，可能的原因：

- 资源不存在。
- 网络连接问题（对于网络资源）。
- 权限不足。
- 资源格式错误。

### ResourceNotWritableException

当尝试写入不支持写入的资源时抛出。

### ResourceWriteFailedException

当资源写入失败时抛出，可能的原因：

- 磁盘空间不足。
- 权限不足。
- 文件被锁定。

## 最佳实践

### 选择合适的资源类型

- **开发环境**: 使用 `classpath:` 资源，便于快速跑通功能。
- **生产环境**: 使用 `file:` 资源，便于运维人员动态修改标记值。
- **默认配置**: 使用 `classpath:` 资源作为默认值。

### 配置更新权限

```properties
# 只读场景（如从远程服务获取）。
datamark.update_allowed=false
```

```properties
# 需要动态更新场景（如本地文件）。
datamark.update_allowed=true
```

### 字符集配置

建议明确指定字符集，避免平台差异：

```properties
datamark.resource_charset=UTF-8
```

### 路径配置

- 使用绝对路径避免路径解析问题。
- 使用相对路径时确保工作目录正确。
- 网络资源确保 URL 可访问。

## 技术实现细节

项目通过 Spring 的 `ApplicationContext.getResource()` 方法获取资源：

```java
import com.dwarfeng.datamark.struct.DatamarkConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;

public class DatamarkResourceManager {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private ApplicationContext ctx;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private DatamarkConfig datamarkConfig;

    public void getResource() {
        Resource resource = ctx.getResource(datamarkConfig.getResourceUrl());
    }
}
```

这种设计使得项目能够利用 Spring 框架的所有资源解析能力，包括：

1. **ResourceLoader 机制**: 支持自定义资源加载器。
2. **协议扩展**: 支持添加新的资源协议。
3. **缓存机制**: Spring 的资源缓存机制。
4. **异常处理**: Spring 统一的异常处理机制。

通过这种设计，dwarfeng-datamark 项目获得了与 Spring 框架完全一致的资源支持能力。
