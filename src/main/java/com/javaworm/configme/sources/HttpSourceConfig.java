package com.javaworm.configme.sources;

import com.javaworm.configme.SourceConfig;

public class HttpSourceConfig implements SourceConfig {
    private String url;
    private double intervalSeconds = 60.0;

    public String getUrl() {
        return url;
    }

    public double getIntervalSeconds() {
        return intervalSeconds;
    }
}
