package com.javaworm.configme.schedulers.http;

import com.javaworm.configme.ConfigSource;
import com.javaworm.configme.sources.HttpSourceConfig;
import java.net.http.HttpRequest.Builder;

public class NoneRequestFilter implements AuthRequestFilter {

  @Override
  public Builder filter(Builder originalBuilder, ConfigSource<HttpSourceConfig> configSource) {
    return originalBuilder;
  }
}
