package com.javaworm.configme.schedulers.http;

import com.javaworm.configme.ConfigSource;
import com.javaworm.configme.sources.HttpSourceConfig;
import java.net.http.HttpRequest;

public interface AuthRequestFilter {
  HttpRequest.Builder filter(
      HttpRequest.Builder originalBuilder, ConfigSource<HttpSourceConfig> configSource);
}
