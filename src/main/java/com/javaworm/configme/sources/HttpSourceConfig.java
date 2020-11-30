package com.javaworm.configme.sources;

import com.javaworm.configme.SourceConfig;

public class HttpSourceConfig implements SourceConfig {
    private String url;
    private int intervalSeconds = 60;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getIntervalSeconds() {
        return intervalSeconds;
    }

    public void setIntervalSeconds(int intervalSeconds) {
        this.intervalSeconds = intervalSeconds;
    }
}
