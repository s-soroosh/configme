package com.javaworm.configme.schedulers;

import com.javaworm.configme.sources.HttpSourceConfig;
import java.net.URI;
import java.net.http.HttpRequest;

public class HttpRequestFactory {
  public HttpRequest create(HttpSourceConfig httpSourceConfig) {
    final var url = httpSourceConfig.getUrl();
    return HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
  }
}
