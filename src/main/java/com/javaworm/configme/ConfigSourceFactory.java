package com.javaworm.configme;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaworm.configme.resources.ConfigSourceResource;
import com.javaworm.configme.sources.HttpSourceConfig;

public class ConfigSourceFactory {

    private final ObjectMapper objectMapper;

    public ConfigSourceFactory(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public ConfigSource create(RequestContext<ConfigSourceResource> context) {
        final String sourceType = context.getResource().getSpec().getSourceType();
        if (sourceType.equals("http")) {
            final HttpSourceConfig httpSourceConfig = this.objectMapper.convertValue(context.getResource().getSpec().getSourceConfig(), HttpSourceConfig.class);
            return new ConfigSource(context.getResource(), httpSourceConfig);
        }
        context.getEventSource().update(context.getResource(), "sorry :(");
        throw new RuntimeException(sourceType + " is not supported source type");
    }
}
