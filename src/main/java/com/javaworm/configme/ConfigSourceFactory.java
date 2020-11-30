package com.javaworm.configme;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaworm.configme.resources.ConfigSourceResource;
import com.javaworm.configme.sources.HttpSourceConfig;

public class ConfigSourceFactory {

    private final ObjectMapper objectMapper;

    public ConfigSourceFactory(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public ConfigSource create(ConfigSourceResource configSourceResource) {
        final String sourceType = configSourceResource.getSpec().getSourceType();
        if (sourceType.equals("http")) {
            final HttpSourceConfig httpSourceConfig = this.objectMapper.convertValue(configSourceResource.getSpec().getSourceConfig(), HttpSourceConfig.class);
            return new ConfigSource(sourceType, httpSourceConfig, configSourceResource.getSpec().getTargetConfigMapName());
        }
        throw new RuntimeException(sourceType + " is not supported source type");
    }
}
