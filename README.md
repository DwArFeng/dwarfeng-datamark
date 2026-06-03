# dwarfeng-datamark

Dwarfeng（赵扶风）的数据标记处理工具，基于 `subgrade` 底座开发，用于提升数据的运维便捷性和可追溯性。

---

## 特性

1. Subgrade 架构支持。
2. 能够轻松地通过配置获取一个数据标记处理器，以获取当前的数据标记值。
3. 数据标记基于 Spring Resource 进行加载。
4. 提供标记刷新 API，可以重复读取 Spring Resource，并刷新数据标记。
5. 提供标记更新 API，当 Spring Resource 支持写入时，可以更新数据标记。
6. 支持通过 `DatamarkQosService` 在单处理器和多处理器场景中统一访问数据标记能力，并使用读写锁提高并发效率。
7. 支持 Spring XML XSD 命名空间配置，以及通过 `@DatamarkField` 和 `DatamarkEntityListener` 在 JPA 实体持久化阶段自动写入数据标记。
8. `dwarfeng-datamark-api` 模块提供 spring-telqos 运维指令示例。

运行 `dwarfeng-datamark-core/src/test` 下的示例以观察核心特性。

| 示例类名                                                              | 说明            |
|-------------------------------------------------------------------|---------------|
| com.dwarfeng.datamark.node.example.MultitonHandlerProcessExample  | 多例模式处理器流程示例   |
| com.dwarfeng.datamark.node.example.MultitonQosProcessExample      | 多例模式 QoS 流程示例 |
| com.dwarfeng.datamark.node.example.SingletonHandlerProcessExample | 单例模式处理器流程示例   |
| com.dwarfeng.datamark.node.example.SingletonQosProcessExample     | 单例模式 QoS 流程示例 |

运行 `dwarfeng-datamark-api/src/test` 下的示例以观察 API 扩展特性。

| 示例类名                                                        | 说明                                      |
|-------------------------------------------------------------|-----------------------------------------|
| com.dwarfeng.datamark.api.integration.example.TelqosExample | Telqos 示例：通过 `datamark` 指令操作数据标记 QoS 服务 |

## 文档

该项目的文档位于 [docs](./docs) 目录下，包括：

### wiki

wiki 为项目的开发人员为本项目编写的详细文档，包含不同语言的版本，主要入口为：

1. [简介](docs/wiki/zh-CN/Introduction.md) - 镜像的 `README.md`，与本文件内容基本相同。
2. [目录](docs/wiki/zh-CN/Contents.md) - 文档目录。

## 安装说明

1. 下载源码。

   使用 git 进行源码下载。

   ```shell
   git clone git@github.com:DwArFeng/dwarfeng-datamark.git
   ```

   对于中国用户，可以使用 gitee 进行高速下载。

   ```shell
   git clone git@gitee.com:dwarfeng/dwarfeng-datamark.git
   ```

2. 项目安装。

   进入项目根目录，执行 maven 命令

   ```shell
   mvn clean source:jar install
   ```

3. 项目引入。

   根工程坐标为 `com.dwarfeng:dwarfeng-datamark`。在业务项目中使用时，通常按需要引入具体模块。

   如果只需要核心数据标记能力，在项目的 `pom.xml` 中添加如下依赖：

   ```xml
   <dependency>
       <groupId>com.dwarfeng</groupId>
       <artifactId>dwarfeng-datamark-core</artifactId>
       <version>${dwarfeng-datamark.version}</version>
   </dependency>
   ```

   如果需要 spring-telqos 运维指令能力，在项目的 `pom.xml` 中添加如下依赖：

   ```xml
   <dependency>
       <groupId>com.dwarfeng</groupId>
       <artifactId>dwarfeng-datamark-api</artifactId>
       <version>${dwarfeng-datamark.version}</version>
   </dependency>
   ```

4. enjoy it.

## 如何使用

1. 运行 `dwarfeng-datamark-core/src/test` 下的 `Example` 以观察核心特性。
2. 运行 `dwarfeng-datamark-api/src/test` 下的 `Example` 以观察 spring-telqos 运维指令特性。
3. 观察项目结构，将其中的配置运用到其它的 subgrade 项目中。

### 单实例模式

加载 `com.dwarfeng.datamark.node.configuration.SingletonConfiguration`，即可获得单例模式的 `DatamarkHandler`、
`DatamarkQosHandler` 与
`DatamarkQosService`。  
在项目的 `application-context-scan.xml` 中追加 `com.dwarfeng.datamark.node.configuration` 包中相应 bean 的扫描，示例如下:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!-- 以下注释用于抑制 idea 中 .md 的警告，实际并无错误，在使用时可以连同本注释一起删除。 -->
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

    <!-- 扫描 node.configuration 包中的 SingletonConfiguration。 -->
    <context:component-scan base-package="com.dwarfeng.datamark.node.configuration" use-default-filters="false">
        <context:include-filter
                type="assignable"
                expression="com.dwarfeng.datamark.node.configuration.SingletonConfiguration"
        />
    </context:component-scan>
</beans>
```

### 多实例模式

不使用包扫描，使用 xml 或者配置类生成 `DatamarkHandlerImpl` 实例。  
在项目的 `bean-definition.xml` 中追加配置，示例如下:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!-- 以下注释用于抑制 idea 中 .md 的警告，实际并无错误，在使用时可以连同本注释一起删除。 -->
<!--suppress SpringBeanConstructorArgInspection, SpringXmlModelInspection, SpringPlaceholdersInspection -->
<beans
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xmlns="http://www.springframework.org/schema/beans"
        xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd"
>
    <!-- 第 1 个实例 -->
    <bean name="configBuilder1" class="com.dwarfeng.datamark.stack.struct.DatamarkConfig.Builder">
        <property name="resourceUrl" value="${datamark.instance1.resource_url}"/>
        <property name="resourceCharset" value="${datamark.instance1.resource_charset}"/>
        <property name="updateAllowed" value="${datamark.instance1.update_allowed}"/>
    </bean>
    <bean name="config1" factory-bean="configBuilder1" factory-method="build"/>
    <bean name="instance1" class="com.dwarfeng.datamark.impl.handler.DatamarkHandlerImpl">
        <constructor-arg name="datamarkConfig" ref="config1"/>
    </bean>

    <!-- 第 2 个实例 -->
    <bean name="configBuilder2" class="com.dwarfeng.datamark.stack.struct.DatamarkConfig.Builder">
        <property name="resourceUrl" value="${datamark.instance2.resource_url}"/>
        <property name="resourceCharset" value="${datamark.instance2.resource_charset}"/>
        <property name="updateAllowed" value="${datamark.instance2.update_allowed}"/>
    </bean>
    <bean name="config2" factory-bean="configBuilder2" factory-method="build"/>
    <bean name="instance2" class="com.dwarfeng.datamark.impl.handler.DatamarkHandlerImpl">
        <constructor-arg name="datamarkConfig" ref="config2"/>
    </bean>
</beans>
```

### XSD 配置

可以使用 `dwarfeng-datamark` 命名空间装配 `DatamarkHandler`、`DatamarkQosHandler` 与 `DatamarkQosService`。  
在项目的 `application-context-datamark.xml` 中追加配置，示例如下:

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

### 任意数量的实例模式

自行设计 `DatamarkHandler` 的工厂类，调用相关工厂方法生成 `DatamarkHandlerImpl` 实例。

需要注意的是：使用者需要自行管理 `DatamarkHandlerImpl` 实例及其依赖的 `ApplicationContext` 与 `DatamarkConfig`。
