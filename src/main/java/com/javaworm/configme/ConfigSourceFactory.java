package com.javaworm.configme;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaworm.configme.resources.ConfigSourceResource;
import com.javaworm.configme.sources.HttpSourceConfig;

public class ConfigSourceFactory {

  public ConfigSourceFactory(ObjectMapper objectMapper) {
  }

  public ConfigSource create(RequestContext<ConfigSourceResource> context) {
    final String sourceType = context.getResource().getSpec().getSourceType();
    if (sourceType.equals("http")) {
      final HttpSourceConfig httpSourceConfig =
          HttpSourceConfig.fromMap(context.getResource().getSpec().getSourceConfig());

      return new ConfigSource(context, httpSourceConfig);
    }
    context.emit("sorry :(");
    throw new RuntimeException(sourceType + " is not supported source type");
  }
}
