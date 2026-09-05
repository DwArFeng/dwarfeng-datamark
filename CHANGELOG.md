# ChangeLog

## Release_3.0.1_20260905_build_A

### 功能构建

- `dwarfeng-datamark-core` 子模块类优化注释、文档注释格式、代码换行格式。
  - com.dwarfeng.datamark.node.configuration.DatamarkHandlerDefinitionParser。
  - com.dwarfeng.datamark.stack.exception.DatamarkExceptionMessagesTest。

- 增加依赖。
  - 增加依赖 `micrometer` 以规避漏洞，版本为 `1.17.0`。

- 优化文件格式。
  - 优化 `pom.xml` 文件的格式。

### Bug 修复

- 修复 Spring 7 / Hibernate 7 环境下 JPA 数据标记实体监听器无法完成构造器依赖注入的问题。

### 功能移除

- (无)

---

## Release_3.0.0_20260828_build_A

### 功能构建

- 更新 README.md。

- Wiki 更新。
  - docs/wiki/zh-CN/ConfigParameters.md。
  - docs/wiki/zh-CN/Introduction.md。
  - docs/wiki/zh-CN/QuickStart.md。
  - docs/wiki/zh-CN/ResourceSupport.md。
  - docs/wiki/zh-CN/UsageGuide.md。

- 升级 JDK 版本至 25。

### Bug 修复

- (无)

### 功能移除

- (无)

---

## 更早的版本

[View all changelogs](./changelogs)
