package com.dwarfeng.datamark.example;

import com.dwarfeng.datamark.service.DatamarkQosService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;
import java.util.Scanner;

/**
 * 单例模式 QoS 流程示例。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
public class SingletonQosProcessExample {

    private static final Logger LOGGER = LoggerFactory.getLogger(SingletonQosProcessExample.class);

    public static void main(String[] args) {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
                "classpath:spring/singleton/application-context*.xml"
        );
        ctx.registerShutdownHook();
        ctx.start();

        DatamarkQosService datamarkQosService = ctx.getBean(DatamarkQosService.class);

        Scanner scanner = new Scanner(System.in);

        // 显示欢迎信息。
        System.out.println("开发者您好!");
        System.out.println("这是一个示例, 用于演示 dwarfeng-datamark 的功能");
        System.out.println("dwarfeng-datamark 是一个数据标记工具, 用于为数据提供当前时刻的标记");
        System.out.println("为了更好的进行体验, 请您在运行本示例之前, 按照文档的说明创建配置文件");
        System.out.print("请按回车键继续...");
        scanner.nextLine();

        // 1. 列出所有处理器的名称。
        System.out.println();
        System.out.println("1. 列出所有处理器的名称...");
        try {
            List<String> handlerNames = datamarkQosService.listHandlerNames();
            System.out.println("可用的处理器名称:");
            for (int i = 0; i < handlerNames.size(); i++) {
                String handlerName = handlerNames.get(i);
                System.out.printf("  %3d: %s%n", i + 1, handlerName);
            }
        } catch (Exception e) {
            LOGGER.warn("获取当前的数据标记值失败, 异常信息如下: ", e);
        }
        System.out.print("请按回车键继续...");
        scanner.nextLine();

        // 2. 获取当前的数据标记值。
        System.out.println();
        System.out.println("2. 获取当前的数据标记值...");
        try {
            System.out.println("请指定处理器名称...");
            String handlerName = scanner.nextLine();
            if (StringUtils.isEmpty(handlerName)) {
                handlerName = null;
            }
            String currentValue = datamarkQosService.get(handlerName);
            System.out.println("处理器 " + handlerName + " 当前的数据标记值为: " + currentValue);
        } catch (Exception e) {
            LOGGER.warn("获取当前的数据标记值失败, 异常信息如下: ", e);
        }
        System.out.print("请按回车键继续...");
        scanner.nextLine();

        // 3. 刷新数据标记值。
        System.out.println();
        System.out.println("3. 刷新数据标记值...");
        try {
            System.out.println("请编辑 ${datamark.resource.url} 对应的资源中的内容, 将其更改为新的数据标记值");
            System.out.print("请按回车键继续...");
            scanner.nextLine();
            System.out.println("请指定处理器名称...");
            String handlerName = scanner.nextLine();
            if (StringUtils.isEmpty(handlerName)) {
                handlerName = null;
            }
            String currentValue = datamarkQosService.refresh(handlerName);
            System.out.println("处理器 " + handlerName + " 当前的数据标记值为: " + currentValue);
            System.out.println("请观察刷新后的数据标记值是否与您编辑后的数据标记值一致");
        } catch (Exception e) {
            LOGGER.warn("刷新并获取当前的数据标记值失败, 异常信息如下: ", e);
        }
        System.out.print("请按回车键继续...");
        scanner.nextLine();

        // 4. 更新数据标记值。
        System.out.println();
        System.out.println("4. 更新数据标记值...");
        try {
            System.out.println("请指定处理器名称...");
            String handlerName = scanner.nextLine();
            if (StringUtils.isEmpty(handlerName)) {
                handlerName = null;
            }
            System.out.println("请指定新的数据标记值...");
            String neoValue = scanner.nextLine();
            String currentValue = datamarkQosService.update(handlerName, neoValue);
            System.out.println("处理器 " + handlerName + " 当前的数据标记值为: " + currentValue);
            System.out.println("请查看 ${datamark.resource.url} 对应的资源中的内容，观察是否与您指定的新的数据标记值一致");
        } catch (Exception e) {
            LOGGER.warn("更新数据标记值失败, 异常信息如下: ", e);
        }
        System.out.print("请按回车键继续...");
        scanner.nextLine();

        // 显示结束信息。
        System.out.println();
        System.out.println("示例演示完毕, 感谢您测试与使用!");

        ctx.stop();
        ctx.close();
        System.exit(0);
    }
}
