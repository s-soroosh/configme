package com.javaworm.configme.sources;

import com.javaworm.configme.SourceConfig;

public class HttpSourceConfig implements SourceConfig {
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
