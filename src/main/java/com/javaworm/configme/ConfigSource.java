package com.javaworm.configme;

import com.javaworm.configme.resources.ConfigSourceResource;

public class ConfigSource<T extends SourceConfig> {
    private final RequestContext<ConfigSourceResource> context;
    private final ConfigSourceResource resource;
    private T sourceConfig;

    public ConfigSource(RequestContext<ConfigSourceResource> context, T sourceConfig) {
        this.context = context;
        this.resource = context.getResource();
        this.sourceConfig = sourceConfig;
    }

    public RequestContext<ConfigSourceResource> getContext() {
        return context;
    }

    public String getSourceType() {
        return resource.getSpec().getSourceType();
    }

    public ConfigSourceResource getResource() {
        return resource;
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
