package com.javaworm.configme.resources;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.Map;

@RegisterForReflection
public class ConfigSourceResourceSpec {
    private String sourceType;
    private Map sourceConfig;
    private String targetConfigMapName;
    private boolean blocking;
    private int blockingTimeoutSeconds;

    public boolean isBlocking() {
        return blocking;
    }

    public long getBlockingTimeoutSeconds() {
        return blockingTimeoutSeconds;
    }

    public String getSourceType() {
        return sourceType;
    }

    public Map getSourceConfig() {
        return sourceConfig;
    }

    public String getTargetConfigMapName() {
        return targetConfigMapName;
    }
}
