# ChangeLog

## Release_2.1.0_20260511_build_A

### 功能构建

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

---

## 更早的版本

[View all changelogs](./changelogs)
