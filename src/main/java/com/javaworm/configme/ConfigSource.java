package com.javaworm.configme;

public class ConfigSource {
    private String sourceType;
    private String namespace;
    private SourceConfig sourceConfig;
    private String targetConfigMapName;

    public ConfigSource(String sourceType, String namespace, SourceConfig sourceConfig, String targetConfigMapName) {
        this.sourceType = sourceType;
        this.namespace = namespace;
        this.sourceConfig = sourceConfig;
        this.targetConfigMapName = targetConfigMapName;
    }
}
