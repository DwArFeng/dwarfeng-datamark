module com.dwarfeng.datamark.core {

    requires com.dwarfeng.datamark.base;
    requires com.dwarfeng.dutil.basic;
    requires com.dwarfeng.subgrade.aop;
    requires com.dwarfeng.subgrade.basic;
    requires jakarta.persistence;
    requires org.apache.commons.lang3;
    requires org.slf4j;
    requires spring.beans;
    requires spring.context;
    requires spring.core;
    requires spring.expression;
    requires org.jetbrains.annotations;
    requires java.xml;

    exports com.dwarfeng.datamark.impl.handler;
    exports com.dwarfeng.datamark.impl.resolve;
    exports com.dwarfeng.datamark.impl.service;
    exports com.dwarfeng.datamark.node.configuration;
    exports com.dwarfeng.datamark.sdk.exception;
    exports com.dwarfeng.datamark.sdk.jpa;
    exports com.dwarfeng.datamark.sdk.util;
    exports com.dwarfeng.datamark.stack.exception;
    exports com.dwarfeng.datamark.stack.handler;
    exports com.dwarfeng.datamark.stack.resolve;
    exports com.dwarfeng.datamark.stack.service;
    exports com.dwarfeng.datamark.stack.struct;
    exports com.dwarfeng.datamark.stack.util;

    opens com.dwarfeng.datamark.stack.i18n to com.dwarfeng.datamark.base;
    opens com.dwarfeng.datamark.sdk.i18n to com.dwarfeng.datamark.base;
    opens com.dwarfeng.datamark.impl.i18n to com.dwarfeng.datamark.base;
}
