# Quick Start - 快速开始

本文档帮助您用最少的步骤体验 `dwarfeng-datamark` 的读取、刷新与更新能力。

## 确认环境

- JDK 1.8 或更高版本。
- Maven 3.x。
- 一个可被 Spring `Resource` 读取的资源。

如果您希望在快速开始中直接体验“更新”功能，建议使用文件系统资源，并将 `update-allowed` 设置为 `true`。

## 引入依赖

在项目的 `pom.xml` 中添加如下依赖：

```xml
<dependency>
    <groupId>com.dwarfeng</groupId>
    <artifactId>dwarfeng-datamark</artifactId>
    <version>${dwarfeng-datamark.version}</version>
</dependency>
```

## 最小化配置

最简单的体验方式是使用项目提供的 XML 命名空间，注册一个 `DatamarkHandler`。

```xml
<?xml version="1.0" encoding="UTF-8"?>
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
            handler-name="singletonHandler"
            resource-url="file:./datamark.storage"
            resource-charset="UTF-8"
            update-allowed="true"
    />
</beans>
```

并在项目目录下准备一个文件 `datamark.storage`，其中写入一行数据，例如：

```text
v1.0.0
```

## 最小示例

完成配置后，即可获取 `DatamarkHandler` 并进行基本操作。

```java
import com.dwarfeng.datamark.handler.DatamarkHandler;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@SuppressWarnings("UnnecessaryModifier")
public class QuickStartExample {

    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
                "classpath:spring/application-context*.xml"
        );
        ctx.registerShutdownHook();
        ctx.start();

        DatamarkHandler datamarkHandler = ctx.getBean(DatamarkHandler.class);

        // 读取当前数据标记值。
        String value = datamarkHandler.get();
        System.out.println("当前数据标记值: " + value);

        // 刷新数据标记值。
        String refreshedValue = datamarkHandler.refresh();
        System.out.println("刷新后的数据标记值: " + refreshedValue);

        // 更新数据标记值。
        String updatedValue = datamarkHandler.update("v1.0.1");
        System.out.println("更新后的数据标记值: " + updatedValue);

        ctx.stop();
        ctx.close();
    }
}
```

## 可观察到的行为

完成上述步骤后，您可以观察到：

1. `get()` 会返回当前缓存中的数据标记值；若尚未加载，则会先读取资源。
2. `refresh()` 会重新读取资源，并刷新内部缓存。
3. `update(...)` 会将新的数据标记值写回资源，并同步更新缓存。

如果 `update_allowed=false`，则调用 `update(...)` 时会抛出异常。

## QoS 的最简用法

如果您需要通过统一入口管理处理器，可以额外启用 QoS：

```xml
<datamark:qos/>
```

启用后，您可以通过 `DatamarkQosService` 对处理器进行统一访问：

- `listHandlerNames()`
- `get(handlerName)`
- `refresh(handlerName)`
- `update(handlerName, value)`

当上下文中只有一个 `DatamarkHandler` 时，`handlerName` 可以为 `null`。

## 运行项目示例

如果您想直接体验项目中自带的示例，可以运行：

- `com.dwarfeng.datamark.example.SingletonHandlerProcessExample`
- `com.dwarfeng.datamark.example.SingletonQosProcessExample`

这两个示例分别演示单例处理器流程与单例 QoS 流程。

## 下一步

- 参阅 [Resource Support](./ResourceSupport.md) 了解项目支持的资源类型。
