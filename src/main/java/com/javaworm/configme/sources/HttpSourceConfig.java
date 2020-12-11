package com.javaworm.configme.sources;

import com.javaworm.configme.SourceConfig;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class HttpSourceConfig implements SourceConfig {
  private String url;
  private double intervalSeconds = 60.0;
  private String httpAuthenticationMethod;
  private BearerAuthenticationConfig authenticationConfig;

  public String getUrl() {
    return url;
  }

  public double getIntervalSeconds() {
    return intervalSeconds;
  }

  public String getHttpAuthenticationMethod() {
    return httpAuthenticationMethod;
  }

  public BearerAuthenticationConfig getAuthenticationConfig() {
    return authenticationConfig;
  }
}
