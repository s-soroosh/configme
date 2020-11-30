package com.javaworm.configme;

public class ConfigSource {
    private String sourceType;
    private SourceConfig sourceConfig;
    private String targetConfigMapName;

    public ConfigSource(String sourceType, SourceConfig sourceConfig, String targetConfigMapName) {
        this.sourceType = sourceType;
        this.sourceConfig = sourceConfig;
        this.targetConfigMapName = targetConfigMapName;
    }
}
