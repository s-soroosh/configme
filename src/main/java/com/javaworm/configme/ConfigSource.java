package com.javaworm.configme;

public class ConfigSource<T extends SourceConfig> {
    private String sourceType;
    private String namespace;
    private T sourceConfig;
    private String targetConfigMapName;

    public ConfigSource(String sourceType, String namespace, T sourceConfig, String targetConfigMapName) {
        this.sourceType = sourceType;
        this.namespace = namespace;
        this.sourceConfig = sourceConfig;
        this.targetConfigMapName = targetConfigMapName;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public SourceConfig getSourceConfig() {
        return sourceConfig;
    }

    public void setSourceConfig(T sourceConfig) {
        this.sourceConfig = sourceConfig;
    }

    public String getTargetConfigMapName() {
        return targetConfigMapName;
    }

    public void setTargetConfigMapName(String targetConfigMapName) {
        this.targetConfigMapName = targetConfigMapName;
    }
}
