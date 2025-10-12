# dwarfeng-datamark

Dwarfeng（赵扶风）的数据标记处理工具，基于 `subgrade` 项目，用于提升数据的运维便捷性和可追溯性。

## 特性

1. Subgrade 架构支持。
2. 能够轻松地通过配置获取一个数据标记处理器，以获取当前的数据标记的值。
3. 数据标记基于 Spring Resource 进行加载。
4. 提供标记刷新 API，可以重复读取 Spring Resource，并刷新数据标记。
5. 提供标记更新 API，当 Spring Resource 支持写入时，可以更新数据标记。
6. 使用读写锁，线程安全的同时提高并发效率。

运行 `src/test` 下的示例以观察全部特性。

| 示例类名                                                         | 说明            |
|--------------------------------------------------------------|---------------|
| com.dwarfeng.datamark.example.MultitonHandlerProcessExample  | 多例模式处理器流程示例   |
| com.dwarfeng.datamark.example.MultitonQosProcessExample      | 多例模式 QoS 流程示例 |
| com.dwarfeng.datamark.example.SingletonHandlerProcessExample | 单例模式处理器流程示例   |
| com.dwarfeng.datamark.example.SingletonQosProcessExample     | 单例模式 QoS 流程示例 |

## 文档

该项目的文档位于 [docs](../../../docs) 目录下，包括：

### wiki

wiki 为项目的开发人员为本项目编写的详细文档，包含不同语言的版本，主要入口为：

1. [简介](./Introduction.md) - 即本文件。
2. [目录](./Contents.md) - 文档目录。

## 安装说明

1. 下载源码。

   - 使用 git 进行源码下载。
      ```shell
      git clone git@github.com:DwArFeng/dwarfeng-datamark.git
      ```

   - 对于中国用户，可以使用 gitee 进行高速下载。
      ```shell
      git clone git@gitee.com:dwarfeng/dwarfeng-datamark.git
      ```

2. 项目安装。

   进入项目根目录，执行 maven 命令
   ```shell
   mvn clean source:jar install
   ```

3. 项目引入。

   在项目的 pom.xml 中添加如下依赖：
   ```xml
   <dependency>
       <groupId>com.dwarfeng</groupId>
       <artifactId>dwarfeng-datamark</artifactId>
       <version>${dwarfeng-datamark.version}</version>
   </dependency>
   ```

4. enjoy it.

## 如何使用

1. 运行 `src/test` 下的 `Example` 以观察全部特性。
2. 观察项目结构，将其中的配置运用到其它的 subgrade 项目中。

### 推荐的使用模式

数据标记处理器的多例模式可以为不同类型的数据提供不同的数据标记处理器，在实际使用中可以细化数据标记的粒度。

数据标记 QoS 可以对所有的数据标记处理器进行统一的管理，一般而言使用单例模式，
除非有特殊需求（比如对不同的数据处理器隔离管理）。

- 数据标记处理器: 推荐使用多例模式，以细化数据标记的粒度。
- 数据标记 QoS: 推荐使用单例模式，以统一管理所有的数据标记处理器。

基于上述原因，推荐使用项目提供的自定义 XML 命名空间进行便捷配置，示例如下：

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

    <!-- 所有的配置均支持 SPEL 表达式。 -->

    <!-- 第 1 个数据标记处理器实例。 -->
    <datamark:handler
            handler-name="instance1"
            resource-url="${datamark.instance1.resource_url}"
            resource-charset="${datamark.instance1.resource_charset}"
            update-allowed="${datamark.instance1.update_allowed}"
    />
    <!-- 第 2 个数据标记处理器实例。 -->
    <datamark:handler
            handler-name="instance2"
            resource-url="${datamark.instance2.resource_url}"
            resource-charset="${datamark.instance2.resource_charset}"
            update-allowed="${datamark.instance2.update_allowed}"
    />

    <!-- 启用 QoS 服务。 -->
    <datamark:qos/>
</beans>
```
