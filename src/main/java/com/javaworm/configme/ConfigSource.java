package com.javaworm.configme;

import com.javaworm.configme.resources.ConfigSourceResource;

public class ConfigSource<T extends SourceConfig> {
    private final ConfigSourceResource resource;
    private T sourceConfig;

    public ConfigSource(ConfigSourceResource resource, T sourceConfig) {
        this.resource = resource;
        this.sourceConfig = sourceConfig;
    }

    public String getSourceType() {
        return resource.getSpec().getSourceType();
    }

    public String getNamespace() {
        return resource.getMetadata().getNamespace();
    }


    public T getSourceConfig() {
        return sourceConfig;
    }

    public String getTargetConfigMapName() {
        return resource.getSpec().getTargetConfigMapName();
    }

    public String getUid() {
        return resource.getMetadata().getUid();
    }

    public String getName() {
        return resource.getMetadata().getName();
    }
}
