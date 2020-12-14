package com.javaworm.configme.schedulers.http;

import com.javaworm.configme.ConfigSource;
import com.javaworm.configme.sources.HttpSourceConfig;
import io.fabric8.kubernetes.api.model.DoneableSecret;
import io.fabric8.kubernetes.api.model.Secret;
import io.fabric8.kubernetes.api.model.SecretList;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.Resource;
import java.net.http.HttpRequest;
import java.util.Base64;

public class BearerRequestFilter {
  private final KubernetesClient k8sClient;
  private MixedOperation<Secret, SecretList, DoneableSecret, Resource<Secret, DoneableSecret>>
      secretsAPI;

  public BearerRequestFilter(KubernetesClient k8sClient) {
    this.k8sClient = k8sClient;
    this.secretsAPI = k8sClient.secrets();
  }

  public HttpRequest.Builder filter(
      HttpRequest.Builder originalBuilder, ConfigSource<HttpSourceConfig> configSource) {
    final var httpSourceConfig = configSource.getSourceConfig();
    originalBuilder.header(
        "Authorization",
        String.format(
            "%s %s",
            httpSourceConfig.getAuthenticationConfig().getTokenType(),
            getSecret(
                configSource.getNamespace(),
                httpSourceConfig.getAuthenticationConfig().getSecretName(),
                httpSourceConfig.getAuthenticationConfig().getTokenSecretKey())));
  }

  private String getSecret(String namespace, String secretName, String secretKeyName) {
    final var secretsAPI = this.secretsAPI.inNamespace(namespace).withName(secretName).get();
    final var encodedSecret = secretsAPI.getData().get(secretKeyName);
    final var secret = new String(Base64.getDecoder().decode(encodedSecret));
    return secret;
  }
}
