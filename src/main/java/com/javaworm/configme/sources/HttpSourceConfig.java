package com.javaworm.configme.sources;

import com.javaworm.configme.SourceConfig;
import io.quarkus.runtime.annotations.RegisterForReflection;
import java.util.Map;

public class HttpSourceConfig implements SourceConfig {
  private String url;
  private double intervalSeconds = 60.0;
  private String httpAuthenticationMethod;
  private BearerAuthenticationConfig authenticationConfig;

  public HttpSourceConfig(
      String url,
      double intervalSeconds,
      String httpAuthenticationMethod,
      BearerAuthenticationConfig authenticationConfig) {
    this.url = url;
    this.intervalSeconds = intervalSeconds;
    this.httpAuthenticationMethod = httpAuthenticationMethod;
    this.authenticationConfig = authenticationConfig;
  }

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

  public static HttpSourceConfig fromMap(Map map) {
    return new HttpSourceConfig(
        map.get("url").toString(), //validate it
        Double.valueOf(map.get("intervalSeconds").toString()),
        map.getOrDefault("authenticationMethod", "none").toString(), //validate is in list
        //        map.get("authenticationConfig")
        null);
  }
}
