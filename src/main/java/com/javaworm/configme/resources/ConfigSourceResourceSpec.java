package com.javaworm.configme.resources;

import java.util.Map;

public class ConfigSourceResourceSpec {
    private String sourceType;
    private Map sourceConfig;
    private String targetConfigMapName;

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public Map getSourceConfig() {
        return sourceConfig;
    }

    public void setSourceConfig(Map sourceConfig) {
        this.sourceConfig = sourceConfig;
    }

    public String getTargetConfigMapName() {
        return targetConfigMapName;
    }

    public void setTargetConfigMapName(String targetConfigMapName) {
        this.targetConfigMapName = targetConfigMapName;
    }
}
