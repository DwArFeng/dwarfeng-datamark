# Usage Guide - 使用指南

## 综述

本文档用于说明 `dwarfeng-datamark` 在真实工程中的常见使用方式，内容覆盖配置、调用、JPA 集成、Telqos 运维及落地建议。

如果您只希望快速体验核心能力，请先阅读 [Quick Start](./QuickStart.md)。
如果您需要了解资源类型与读写限制，请结合 [Resource Support](./ResourceSupport.md) 一起阅读。

`dwarfeng-datamark` 的核心目标是为数据写入统一的数据标记值，并提供可刷新、可更新、可运维查询的处理能力。

## 快速开始

### 添加依赖

在项目的 `pom.xml` 中添加如下依赖：

```xml
<dependency>
    <groupId>com.dwarfeng</groupId>
    <artifactId>dwarfeng-datamark</artifactId>
    <version>${dwarfeng-datamark.version}</version>
</dependency>
```

### 最小化配置

最小化配置方式是注册一个 `datamark:handler`：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!-- 以下注释用于抑制 idea 中 .md 的警告，实际并无错误，在使用时可以连同本注释一起删除。, XmlDefaultAttributeValue -->
<!--suppress SpringBeanConstructorArgInspection, SpringXmlModelInspection, SpringPlaceholdersInspection -->
<!--suppress XmlDefaultAttributeValue -->
<beans
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xmlns:datamark="http://dwarfeng.com/schema/dwarfeng-datamark"
        xmlns="http://www.springframework.org/schema/beans"
        xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://dwarfeng.com/schema/dwarfeng-datamark
        http://dwarfeng.com/schema/dwarfeng-datamark/dwarfeng-datamark.xsd"
>

    <datamark:handler
            handler-name="datamarkHandler"
            resource-url="file:./datamark.storage"
            resource-charset="UTF-8"
            update-allowed="true"
    />
</beans>
```

如果需要使用 QoS（服务质量）服务，可以追加：

```xml
<datamark:qos/>
```

QoS 服务的典型应用场景包括：

- 通过 `spring-telqos` 提供的 `DatamarkCommand` 进行运维查询与更新。

### 最小调用示例

```java
import com.dwarfeng.datamark.handler.DatamarkHandler;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@SuppressWarnings("UnnecessaryModifier")
public class UsageGuideQuickExample {

    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
                "classpath:spring/application-context*.xml"
        );
        ctx.registerShutdownHook();
        ctx.start();

        DatamarkHandler handler = ctx.getBean(DatamarkHandler.class);

        String value = handler.get();
        String refreshed = handler.refresh();
        String updated = handler.update("v1.0.1");

        System.out.println("value = " + value);
        System.out.println("refreshed = " + refreshed);
        System.out.println("updated = " + updated);

        ctx.stop();
        ctx.close();
    }
}
```

## 配置详解

`dwarfeng-datamark` 提供两个命名空间元素：

- `datamark:handler`：配置单个数据标记处理器。
- `datamark:qos`：配置 QoS 统一访问入口。

### datamark:handler

`datamark:handler` 支持如下属性。

#### handler-name

- 属性名：`handler-name`。
- 类型：`String`。
- 默认值：`datamarkHandler`。
- 说明：处理器 bean 名称。多处理器场景下，QoS 通过此名称路由。

#### resource-url

- 属性名：`resource-url`。
- 类型：`String`。
- 默认值：`classpath:datamark/default.storage`（由默认构建器提供）。
- 说明：Spring Resource 地址，可使用 `classpath:`、`file:` 等。

#### resource-charset

- 属性名：`resource-charset`。
- 类型：`String`。
- 默认值：平台默认字符集（由默认构建器提供）。
- 说明：资源读取/写入字符集。建议显式指定为 `UTF-8`。

#### update-allowed

- 属性名：`update-allowed`。
- 类型：`Boolean`。
- 默认值：`false`（由默认构建器提供）。
- 说明：是否允许调用 `update(...)`。关闭后调用更新会抛出异常。

### datamark:qos

`datamark:qos` 支持如下属性。

#### handler-name

- 属性名：`handler-name`。
- 类型：`String`。
- 默认值：`datamarkQosHandler`。
- 说明：QoS handler 的 bean 名称。

#### service-name

- 属性名：`service-name`。
- 类型：`String`。
- 默认值：`datamarkQosService`。
- 说明：QoS service 的 bean 名称。

## 单例与多例集成模式

### 单例模式

单例模式下，应用上下文中只有一个 `DatamarkHandler`。可通过配置类扫描快速启用：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!-- 以下注释用于抑制 idea 中 .md 的警告，实际并无错误，在使用时可以连同本注释一起删除。, XmlDefaultAttributeValue -->
<!--suppress SpringXmlModelInspection -->
<beans
        xmlns:context="http://www.springframework.org/schema/context"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xmlns="http://www.springframework.org/schema/beans"
        xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        http://www.springframework.org/schema/context/spring-context.xsd"
>

    <context:component-scan base-package="com.dwarfeng.datamark.configuration" use-default-filters="false">
        <context:include-filter
                type="assignable" expression="com.dwarfeng.datamark.configuration.ServiceExceptionMapperConfiguration"
        />
        <context:include-filter
                type="assignable" expression="com.dwarfeng.datamark.configuration.SingletonHandlerConfiguration"
        />
        <context:include-filter
                type="assignable" expression="com.dwarfeng.datamark.configuration.SingletonQosConfiguration"
        />
    </context:component-scan>
</beans>
```

在该模式下，通过 `DatamarkQosService` 调用时，`handlerName` 可传 `null`。

### 多例模式

多例模式通常用于不同数据域使用不同数据标记值，示例如下：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!-- 以下注释用于抑制 idea 中 .md 的警告，实际并无错误，在使用时可以连同本注释一起删除。 -->
<!--suppress SpringBeanConstructorArgInspection, SpringXmlModelInspection, SpringPlaceholdersInspection -->
<beans
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xmlns:datamark="http://dwarfeng.com/schema/dwarfeng-datamark"
        xmlns="http://www.springframework.org/schema/beans"
        xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://dwarfeng.com/schema/dwarfeng-datamark
        http://dwarfeng.com/schema/dwarfeng-datamark/dwarfeng-datamark.xsd"
>

    <datamark:handler
            handler-name="foobarCategoryDatamarkHandler"
            resource-url="${datamark.foobar_category.resource_url}"
            resource-charset="${datamark.foobar_category.resource_charset}"
            update-allowed="${datamark.foobar_category.update_allowed}"
    />
    <datamark:handler
            handler-name="foobarNodeDatamarkHandler"
            resource-url="${datamark.foobar_node.resource_url}"
            resource-charset="${datamark.foobar_node.resource_charset}"
            update-allowed="${datamark.foobar_node.update_allowed}"
    />

    <datamark:qos/>
</beans>
```

多例模式下，调用 QoS 时应明确传入 `handlerName`，避免路由歧义。

## 资源与更新能力

`dwarfeng-datamark` 的资源加载依赖 Spring Resource，因此可支持 Spring 可解析的资源地址。

更新行为同时受两个条件约束：

- 配置层：`update-allowed=true`。
- 资源层：目标资源必须可写（通常是 `WritableResource`）。

常见建议如下：

- 开发演示：`classpath:` 资源，仅用于读取体验。
- 生产运维：`file:` 资源，便于运维动态更新标记值。
- 统一字符集：建议固定 `UTF-8`，减少跨平台乱码问题。

## JPA 自动写入集成

在 JPA 实体中集成数据标记时，需要同时使用：

- `@EntityListeners(DatamarkEntityListener.class)`。
- `@DatamarkField`。

以下示例为脱敏后的最小实体片段，仅保留与数据标记相关内容：

```java
import com.dwarfeng.datamark.bean.jpa.DatamarkEntityListener;
import com.dwarfeng.datamark.bean.jpa.DatamarkField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_foobar_node")
@EntityListeners(DatamarkEntityListener.class)
public class HibernateFoobarNode {

    @Id
    @Column(name = "string_id", nullable = false, length = 50)
    private String stringId;

    @DatamarkField(handlerName = "foobarNodeDatamarkHandler")
    @Column(
            name = "created_datamark",
            length = com.dwarfeng.datamark.util.Constraints.LENGTH_DATAMARK_VALUE,
            updatable = false
    )
    private String createdDatamark;

    @DatamarkField(handlerName = "foobarNodeDatamarkHandler")
    @Column(
            name = "modified_datamark",
            length = com.dwarfeng.datamark.util.Constraints.LENGTH_DATAMARK_VALUE
    )
    private String modifiedDatamark;

    public HibernateFoobarNode() {
    }

    public String getStringId() {
        return stringId;
    }

    public void setStringId(String stringId) {
        this.stringId = stringId;
    }

    public String getCreatedDatamark() {
        return createdDatamark;
    }

    public void setCreatedDatamark(String createdDatamark) {
        this.createdDatamark = createdDatamark;
    }

    public String getModifiedDatamark() {
        return modifiedDatamark;
    }

    public void setModifiedDatamark(String modifiedDatamark) {
        this.modifiedDatamark = modifiedDatamark;
    }
}
```

实体在 `@PrePersist/@PreUpdate` 阶段会由监听器写入对应数据标记值。

## Telqos 运维命令

如果工程集成了 `spring-telqos`，并注册了 `DatamarkCommand`，可使用 `datamark` 指令进行运维。

命令语法如下：

```text
usage: datamark -lh
datamark -ua [-hn handler-name]
datamark -get [-hn handler-name]
datamark -refresh [-hn handler-name]
datamark -update [-hn handler-name] [-dv datamark-value]
```

### 示例 - 列出处理器

```text
datamark -lh
可用的处理器名称:
    1: foobarCategoryDatamarkHandler
    2: foobarNodeDatamarkHandler
OK
```

### 示例 - 查询是否允许更新

```text
datamark -ua -hn foobarCategoryDatamarkHandler
处理器名称: foobarCategoryDatamarkHandler, 允许更新: true
OK
```

### 示例 - 获取数据标记值

```text
datamark -get -hn foobarCategoryDatamarkHandler
处理器名称: foobarCategoryDatamarkHandler, 数据标记值: foobar-node
OK
```

### 示例 - 刷新数据标记值

```text
datamark -refresh -hn foobarCategoryDatamarkHandler
刷新成功!
处理器名称: foobarCategoryDatamarkHandler, 刷新后的数据标记值: foobar-node
OK
```

### 示例 - 更新数据标记值

```text
datamark -update -hn foobarCategoryDatamarkHandler -dv foobar
更新成功!
处理器名称: foobarCategoryDatamarkHandler, 更新的数据标记值: foobar
OK
```

## 实际工程中的推荐落地方式

以下模式来自已验证工程实践，示例做了业务脱敏。

### 配置拆分建议

建议将数据标记配置单独放入 `conf/datamark/settings.properties`：

```properties
#---------------------------------配置说明----------------------------------------
# 数据标记资源的 URL，格式参考 Spring 资源路径。
# datamark.xxx.resource_url=classpath:datamark/default.storage
# 数据标记资源的字符集。
# datamark.xxx.resource_charset=UTF-8
# 数据标记服务是否允许更新。
# datamark.xxx.update_allowed=true

#---------------------------------FoobarCategory----------------------------------------
datamark.foobar_category.resource_url=file:conf/datamark/current-foobar-category.storage
datamark.foobar_category.resource_charset=UTF-8
datamark.foobar_category.update_allowed=true

#---------------------------------FoobarNode----------------------------------------
datamark.foobar_node.resource_url=file:conf/datamark/current-foobar-node.storage
datamark.foobar_node.resource_charset=UTF-8
datamark.foobar_node.update_allowed=true
```

### 命名规划建议

建议统一采用：

- 处理器名称：`{domain}DatamarkHandler`。
- 配置前缀：`datamark.{domain}.*`。

例如：

- `foobarCategoryDatamarkHandler` 对应 `datamark.foobar_category.*`。
- `foobarNodeDatamarkHandler` 对应 `datamark.foobar_node.*`。

### 运维建议

- 将标记资源放在运维可控目录，避免应用重打包才能修改。
- 保留更新操作日志，便于追溯何时、由谁、将标记值更新为哪个值。
- 将 `datamark` telqos 指令纳入发布后巡检流程。

## 最佳实践

### 数据标记值规范

- 建议使用可读、可追踪的值，例如：`release-2026.04.29`。
- 避免使用空字符串或不可识别随机值。
- 长度需要满足字段长度约束，典型场景建议直接使用 `Constraints.LENGTH_DATAMARK_VALUE`。

### 异常处理建议

- 调用层不要吞掉 `ServiceException` / `HandlerException`。
- 对 `update` 失败区分是“策略禁止更新”还是“资源不可写”。
- 多处理器场景下，错误信息中应包含 `handlerName`。

### 多例场景建议

- 严格区分不同数据域的处理器，避免多个数据域共用一个标记资源。
- `DatamarkQosService` 调用统一经过封装层，减少重复的 `handlerName` 判空与校验代码。

## 常见问题

### update 调用失败，提示不允许更新

请检查 `update-allowed` 是否为 `true`。

### update 调用失败，提示资源不可写

请检查资源是否为可写资源。`classpath:` 通常只读，建议切换为 `file:`。

### 中文乱码

请显式配置 `resource-charset`，建议使用 `UTF-8`。在中文 Windows 环境下，也可按终端与文件编码情况评估 `GBK`。

### 多例场景提示找不到处理器

请确认传入的 `handlerName` 与 `datamark:handler` 的 `handler-name` 完全一致。

### 单处理器场景是否必须传 handlerName

不必须。当且仅当应用上下文中仅有一个 `DatamarkHandler` 时，QoS 接口允许 `handlerName` 传 `null`。

## 参阅

- [Quick Start](./QuickStart.md) - 快速开始，用最少的步骤体验 `dwarfeng-datamark` 的读取、刷新与更新能力。
- [Resource Support](./ResourceSupport.md) - 资源支持说明，详细介绍项目支持的资源类型和配置方法。
