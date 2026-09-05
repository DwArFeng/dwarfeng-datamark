module com.dwarfeng.datamark.api {

    requires com.dwarfeng.datamark.base;
    requires com.dwarfeng.datamark.core;
    requires com.dwarfeng.springtelqos.core;
    requires org.apache.commons.cli;
    requires org.apache.commons.lang3;
    requires static org.jetbrains.annotations;

    exports com.dwarfeng.datamark.api.integration.springtelqos;

    opens com.dwarfeng.datamark.api.i18n to com.dwarfeng.datamark.base;
}
