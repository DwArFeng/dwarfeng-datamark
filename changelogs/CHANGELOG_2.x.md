# ChangeLog

## Release_2.2.1_20260828_build_A

### 功能构建

- 更新 README.md。

- 项目文档升级。
    - docs/wiki/zh-CN/ConfigParameters.md。
    - docs/wiki/zh-CN/Contents.md。
    - docs/wiki/zh-CN/Introduction.md。
    - docs/wiki/zh-CN/QuickStart.md。
    - docs/wiki/zh-CN/ResourceSupport.md。
    - docs/wiki/zh-CN/UsageGuide.md。
    - docs/wiki/zh-CN/VersionBlacklist.md。

- 优化部分示例的控制台输出方法。
    - com.dwarfeng.datamark.api.integration.example.TelqosExample。
    - com.dwarfeng.datamark.node.example.MultitonHandlerProcessExample。
    - com.dwarfeng.datamark.node.example.MultitonQosProcessExample。
    - com.dwarfeng.datamark.node.example.SingletonHandlerProcessExample。
    - com.dwarfeng.datamark.node.example.SingletonQosProcessExample。

- 依赖升级。
    - 升级 `subgrade` 依赖版本为 `1.8.4.a` 以规避漏洞。
    - 升级 `spring-telqos` 依赖版本为 `2.0.3.a` 以规避漏洞。

### Bug 修复

- (无)

### 功能移除

- (无)

---

## Release_2.2.0_20260603_build_A

### 功能构建

- 更新 README.md。

- Wiki 更新。
    - docs/wiki/zh-CN/ConfigParameters.md。
    - docs/wiki/zh-CN/Introduction.md。

- 项目单例配置优化。
    - 新增 `com.dwarfeng.datamark.node.configuration.SingletonConfiguration`，实现完整的单例配置功能。

- 项目异常机制优化。
    - 新增 `com.dwarfeng.datamark.sdk.util.DatamarkExceptionHelper` 工具类。
    - 新增 `com.dwarfeng.datamark.sdk.util.DatamarkQosExceptionHelper` 工具类。
    - 优化 `com.dwarfeng.datamark.impl.handler.DatamarkHandlerImpl` 中的异常处理逻辑。
    - 优化 `com.dwarfeng.datamark.impl.handler.DatamarkQosHandlerImpl` 中的异常处理逻辑。

- 项目结构优化。
    - 将 `MultitonHandlerProcessExample` 移动至 `com.dwarfeng.datamark.node.example` 包下。
    - 将 `MultitonQosProcessExample` 移动至 `com.dwarfeng.datamark.node.example` 包下。
    - 将 `SingletonHandlerProcessExample` 移动至 `com.dwarfeng.datamark.node.example` 包下。
    - 将 `SingletonQosProcessExample` 移动至 `com.dwarfeng.datamark.node.example` 包下。

- 优化项目的 XSD 配置逻辑。
    - 将元素属性的默认值提升至 XSD 中进行定义，以简化代码实现。
    - 优化命名空间解析器工具类。
    - `com.dwarfeng.datamark.node.configuration.DatamarkHandlerDefinitionParser` 解析逻辑优化。
    - `com.dwarfeng.datamark.node.configuration.DatamarkQosDefinitionParser` 解析逻辑优化。

### Bug 修复

- 修正 `dwarfeng-datamark-api` 子模块部分 `properties` 文件错误的字符集。
    - telqos/connection.properties。

### 功能移除

- (无)

---

## Release_2.1.1_20260527_build_A

### 功能构建

- Wiki 更新。
    - docs/wiki/zh-CN/UseWithMaven.md。

- `dwarfeng-datamark-api` 子模块配置文件优化。
    - telqos/connection.properties。

- 依赖升级。
    - 升级 `subgrade` 依赖版本为 `1.8.3.a` 以规避漏洞。
    - 升级 `spring-telqos` 依赖版本为 `2.0.2.a` 以规避漏洞。

### Bug 修复

- (无)

### 功能移除

- (无)

---

## Release_2.1.0_20260513_build_A

### 功能构建

- Wiki 编写。
    - docs/wiki/zh-CN/ConfigParameters.md。

- 新增监听器解析器。
    - 新增 `com.dwarfeng.datamark.stack.resolve.ListenerResolver` 接口，实现监听器处理器名称解析。
    - 新增 `com.dwarfeng.datamark.impl.resolve.IntrinsicListenerResolver` 本征实现。
    - 调整 `com.dwarfeng.datamark.sdk.jpa.DatamarkEntityListener` 的内部实现，以应用相关逻辑。
    - 新增监听器解析器相关异常及服务异常映射支持。

### Bug 修复

- (无)

### 功能移除

- (无)

---

## Release_2.0.1_20260510_build_A

### 功能构建

- Wiki 编写。
    - docs/wiki/zh-CN/UseWithMaven.md。

- 优化部分类中字段的注解。
    - com.dwarfeng.datamark.api.integration.springtelqos.DatamarkCommand。

### Bug 修复

- (无)

### 功能移除

- (无)

---

## Release_2.0.0_20260508_build_A

### 功能构建

- 更新 README.md。

- Wiki 更新。
    - docs/wiki/zh-CN/UsageGuide.md。
    - docs/wiki/zh-CN/Introduction.md。

- 优化部分运维指令功能及代码结构。
    - com.dwarfeng.datamark.api.integration.springtelqos.DatamarkCommand。

- 重构项目模块。
    - 新增 `dwarfeng-datamark-core` 子模块，并迁移原有代码至该模块。
    - 新增 `dwarfeng-datamark-api` 子模块。

- 重构项目结构。
    - 将项目构型更改为 subgrade 稳健式标准构型。

- 依赖升级。
    - 升级 `spring-telqos` 依赖版本为 `2.0.0.a` 并解决兼容性问题，以规避漏洞。

### Bug 修复

- (无)

### 功能移除

- (无)
