package com.dwarfeng.datamark.bean.jpa;

import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.*;

/**
 * 数据标记字段。
 *
 * <p>
 * 该注解应该被添加到 JPA 实体中，与 JPA 注解一同使用。
 *
 * <p>
 * 示例代码如下：
 * <blockquote><pre>
 * {@literal @}Entity
 * {@literal @}Table(name = "tbl_user")
 * {@literal @}EntityListeners(DatamarkEntityListener.class)
 * public class HibernateUser implements Bean {
 *
 *     // 省略之前的代码...
 *
 *     // 使用 DatamarkField
 *     {@literal @}DatamarkField
 *     {@literal @}Column(
 *             name = "created_datamark",
 *             length = Constraints.LENGTH_DATAMARK,
 *             updatable = false
 *     )
 *     private String createdDatamark;
 *
 *     {@literal @}DatamarkField
 *     {@literal @}Column(
 *             name = "modified_datamark",
 *             length = Constraints.LENGTH_DATAMARK
 *     )
 *     private String modifiedDatamark;
 *
 *     // 省略之后的代码...
 * }
 * </pre></blockquote>
 *
 * @author DwArFeng
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface DatamarkField {

    /**
     * 处理器名称。
     *
     * <p>
     * 该参数为对应 {@link com.dwarfeng.datamark.handler.DatamarkHandler} 实例的 <code>bean name</code>。
     *
     * <p>
     * 当应用上下文中只有一个 {@link com.dwarfeng.datamark.handler.DatamarkHandler} 时，该参数可以不指定（为空字符串）。
     */
    String handlerName() default StringUtils.EMPTY;
}
