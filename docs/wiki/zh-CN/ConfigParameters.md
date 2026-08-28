# Config Parameters - 配置参数详解

本文档详细说明了 dwarfeng-datamark 的配置参数，包括各配置项的含义、默认值、校验规则以及配置示例。
配置参数可以通过 properties 文件、Spring 占位符或 `dwarfeng-datamark` XML 命名空间注入，
最终构建为 `DatamarkConfig` 对象供 `DatamarkHandlerImpl` 使用。

## 配置加载方式

### 单例模式

使用 `SingletonConfiguration` 时，通过 Spring 的 `@Value` 注解从 properties 中读取配置。
需要确保 Spring 的 `property-placeholder` 已加载包含 `datamark.*` 前缀的配置文件。

示例配置文件位于 `classpath:datamark/singleton/settings.properties`，
本地覆盖文件通过 `file:conf/test.datamark/singleton/*.properties` 指定。

### 多实例模式

使用 XML 或配置类手动创建多个 `DatamarkHandlerImpl` 实例时，通过占位符区分不同实例的配置。
例如：`${datamark.instance1.resource_url}`、`${datamark.instance1.resource_charset}` 对应第一个实例，
`${datamark.instance2.resource_url}`、`${datamark.instance2.resource_charset}` 对应第二个实例。

示例配置文件位于 `classpath:datamark/multiton/settings.properties`，
本地覆盖文件通过 `file:conf/test.datamark/multiton/*.properties` 指定。

## 基础资源参数

### datamark.resource_url

数据标记资源的 URL，支持 Spring `Resource` 可解析的资源路径。
类型：String。
默认值：`classpath:datamark/default.storage`。

### datamark.resource_charset

数据标记资源的字符集，用于读取与写入数据标记值。
类型：String。
默认值：`Charset.defaultCharset().name()`（常见为 UTF-8，具体取决于运行环境）。

## 更新控制参数

### datamark.update_allowed

是否允许更新数据标记值。`false` 时调用 `DatamarkHandler.update()` 会抛出异常。类型：boolean，默认值：false。

## 配置示例

### 单例模式

在 `settings.properties` 中配置：

```properties
# 数据标记资源的 URL。
datamark.resource_url=classpath:datamark/default.storage
# 数据标记资源的字符集。
datamark.resource_charset=UTF-8
# 数据标记是否允许更新。
datamark.update_allowed=false
```

### 多实例模式

在 Spring XML 中为多个实例配置不同的占位符：

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
    <!-- 第 1 个实例。 -->
    <datamark:handler
            handler-name="instance1"
            resource-url="${datamark.instance1.resource_url}"
            resource-charset="${datamark.instance1.resource_charset}"
            update-allowed="${datamark.instance1.update_allowed}"
    />

    <!-- 第 2 个实例。 -->
    <datamark:handler
            handler-name="instance2"
            resource-url="${datamark.instance2.resource_url}"
            resource-charset="${datamark.instance2.resource_charset}"
            update-allowed="${datamark.instance2.update_allowed}"
    />

    <!-- 第 3 个实例。 -->
    <datamark:handler
            handler-name="instance3"
            resource-url="${datamark.instance3.resource_url}"
            resource-charset="${datamark.instance3.resource_charset}"
            update-allowed="${datamark.instance3.update_allowed}"
    />

    <datamark:qos/>
</beans>
```

在 properties 中对应配置 `datamark.instance1.resource_url`、`datamark.instance2.resource_url` 等。

### XSD 配置模式

在 Spring XML 中使用 `dwarfeng-datamark` 命名空间：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!-- 以下注释用于抑制 idea 中 .md 的警告，实际并无错误，在使用时可以连同本注释一起删除。 -->
<!--suppress SpringPlaceholdersInspection -->
<beans
        xmlns:datamark="http://dwarfeng.com/schema/dwarfeng-datamark"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xmlns="http://www.springframework.org/schema/beans"
        xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://dwarfeng.com/schema/dwarfeng-datamark
        http://dwarfeng.com/schema/dwarfeng-datamark/dwarfeng-datamark.xsd"
>

    <datamark:handler
            handler-name="instance1"
            resource-url="${datamark.instance1.resource_url}"
            resource-charset="${datamark.instance1.resource_charset}"
            update-allowed="${datamark.instance1.update_allowed}"
    />
    <datamark:handler
            handler-name="instance2"
            resource-url="${datamark.instance2.resource_url}"
            resource-charset="${datamark.instance2.resource_charset}"
            update-allowed="${datamark.instance2.update_allowed}"
    />
    <datamark:qos/>
</beans>
```

`handler` 元素可通过 `handler-name`、`resource-url`、`resource-charset`、`update-allowed`
指定处理器 bean 名称、资源地址、资源字符集和是否允许更新。

`qos` 元素可通过 `qos-handler-name`、`qos-service-name`、`sem-ref`
指定 QoS 处理器 bean 名称、QoS 服务 bean 名称和 `ServiceExceptionMapper` 引用。

## 参数校验规则

配置在构建 `DatamarkConfig` 时会进行校验，校验逻辑由 `DatamarkConfigUtil` 实现。常见约束如下：

- 资源 URL 不能为 null。
- 资源字符集不能为 null。
- 资源字符集不能为空字符串。
- 资源字符集必须是 `Charset.forName` 可解析的合法字符集名称。
- 更新开关参数无额外约束。

违反上述规则时，将抛出 `NullPointerException` 或 `IllegalArgumentException`。

## 参阅

- [Quick Start](./QuickStart.md) - 快速开始，用最少的步骤体验本项目。
- [Usage Guide](./UsageGuide.md) - 使用指南，介绍了项目在工程中的配置、集成与运维方式。
