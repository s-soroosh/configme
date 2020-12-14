package com.javaworm.configme.schedulers.http;

import com.javaworm.configme.ConfigSource;
import com.javaworm.configme.sources.HttpSourceConfig;
import io.fabric8.kubernetes.api.model.DoneableSecret;
import io.fabric8.kubernetes.api.model.Secret;
import io.fabric8.kubernetes.api.model.SecretList;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.Resource;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.Base64;

public class HttpRequestFactory {
  private MixedOperation<Secret, SecretList, DoneableSecret, Resource<Secret, DoneableSecret>>
      secretsAPI;

  public HttpRequestFactory(KubernetesClient k8sClient) {
    this.secretsAPI = k8sClient.secrets();
  }

  public HttpRequest create(ConfigSource<HttpSourceConfig> configSource) {
    final var httpSourceConfig = configSource.getSourceConfig();
    final var url = httpSourceConfig.getUrl();
    final var requestBuilder = HttpRequest.newBuilder().uri(URI.create(url)).GET();
    if (httpSourceConfig.getHttpAuthenticationMethod().equals("bearer")) {
      requestBuilder.header(
          "Authorization",
          String.format(
              "%s %s",
              httpSourceConfig.getAuthenticationConfig().getTokenType(),
              getSecret(
                  configSource.getNamespace(),
                  httpSourceConfig.getAuthenticationConfig().getSecretName(),
                  httpSourceConfig.getAuthenticationConfig().getTokenSecretKey())));
    }

    return requestBuilder.build();
  }

  private String getSecret(String namespace, String secretName, String secretKeyName) {
    final var secretsAPI = this.secretsAPI.inNamespace(namespace).withName(secretName).get();
    final var encodedSecret = secretsAPI.getData().get(secretKeyName);
    System.out.println(encodedSecret);
    final var secret = new String(Base64.getDecoder().decode(encodedSecret));
    System.out.println(secret);
    return secret;
  }
}
