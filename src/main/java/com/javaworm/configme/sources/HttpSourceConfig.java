package com.javaworm.configme.sources;

import com.javaworm.configme.SourceConfig;
import java.util.Map;

public class HttpSourceConfig implements SourceConfig {
  private final String url;
  private final double intervalSeconds;
  private final String httpAuthenticationMethod;
  private final BearerAuthenticationConfig authenticationConfig;

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

  public static HttpSourceConfig fromMap(Map<Object, Object> map) {
    final var authenticationConfigMap =
        (Map<Object, Object>) map.getOrDefault("authenticationConfig", null);
    final BearerAuthenticationConfig authenticationConfig;
    if (authenticationConfigMap == null) {
      authenticationConfig = null;
    } else {
      authenticationConfig =
          new BearerAuthenticationConfig(
              authenticationConfigMap.get("tokenType").toString(),
              authenticationConfigMap.get("secretName").toString(),
              authenticationConfigMap.get("tokenSecretKey").toString());
    }

    return new HttpSourceConfig(
        map.get("url").toString(), // validate it
        Double.parseDouble(map.getOrDefault("intervalSeconds", "60").toString()),
        map.getOrDefault("authenticationMethod", "none").toString(), // validate is in list
        authenticationConfig);
  }
}
