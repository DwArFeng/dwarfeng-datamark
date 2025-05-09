package com.dwarfeng.datamark.struct;

import com.dwarfeng.datamark.util.DatamarkConfigUtil;
import com.dwarfeng.dutil.basic.prog.Buildable;

import java.nio.charset.Charset;

/**
 * 数据标记配置。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
public class DatamarkConfig {

    private final String resourceUrl;
    private final String resourceCharset;
    private final boolean updateAllowed;

    public DatamarkConfig(String resourceUrl, String resourceCharset, boolean updateAllowed) {
        this(resourceUrl, resourceCharset, updateAllowed, false);
    }

    private DatamarkConfig(
            String resourceUrl, String resourceCharset, boolean updateAllowed, boolean paramReliable
    ) {
        // 如果参数不可靠，则检查参数。
        if (!paramReliable) {
            DatamarkConfigUtil.checkResourceUrl(resourceUrl);
            DatamarkConfigUtil.checkResourceCharset(resourceCharset);
            DatamarkConfigUtil.checkUpdateAllowed(updateAllowed);
        }
        // 设置值。
        this.resourceUrl = resourceUrl;
        this.resourceCharset = resourceCharset;
        this.updateAllowed = updateAllowed;
    }

    public String getResourceUrl() {
        return resourceUrl;
    }

    public String getResourceCharset() {
        return resourceCharset;
    }

    public boolean isUpdateAllowed() {
        return updateAllowed;
    }

    @Override
    public String toString() {
        return "DatamarkConfig{" +
                "resourceUrl='" + resourceUrl + '\'' +
                ", resourceCharset='" + resourceCharset + '\'' +
                ", updateAllowed=" + updateAllowed +
                '}';
    }

    /**
     * 数据标记配置构造器。
     *
     * @author DwArFeng
     * @since 1.0.0
     */
    public static final class Builder implements Buildable<DatamarkConfig> {

        public static final String DEFAULT_RESOURCE_URL = "classpath:datamark/default.storage";
        public static final String DEFAULT_RESOURCE_CHARSET = Charset.defaultCharset().name();
        public static final boolean DEFAULT_UPDATE_ALLOWED = false;

        private String resourceUrl = DEFAULT_RESOURCE_URL;
        private String resourceCharset = DEFAULT_RESOURCE_CHARSET;
        private boolean updateAllowed = DEFAULT_UPDATE_ALLOWED;

        public Builder() {
        }

        public Builder setResourceUrl(String resourceUrl) {
            this.resourceUrl = resourceUrl;
            return this;
        }

        public Builder setResourceCharset(String resourceCharset) {
            this.resourceCharset = resourceCharset;
            return this;
        }

        public Builder setUpdateAllowed(boolean updateAllowed) {
            this.updateAllowed = updateAllowed;
            return this;
        }

        @Override
        public DatamarkConfig build() {
            // 检查参数。
            DatamarkConfigUtil.checkResourceUrl(resourceUrl);
            DatamarkConfigUtil.checkResourceCharset(resourceCharset);
            DatamarkConfigUtil.checkUpdateAllowed(updateAllowed);

            // 构造并返回配置。
            return new DatamarkConfig(resourceUrl, resourceCharset, updateAllowed, true);
        }

        @Override
        public String toString() {
            return "Builder{" +
                    "resourceUrl='" + resourceUrl + '\'' +
                    ", resourceCharset='" + resourceCharset + '\'' +
                    ", updateAllowed=" + updateAllowed +
                    '}';
        }
    }
}
