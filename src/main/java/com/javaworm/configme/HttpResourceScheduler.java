package com.javaworm.configme;

import com.javaworm.configme.sources.HttpSourceConfig;
import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.api.model.ConfigMapBuilder;
import io.fabric8.kubernetes.api.model.DoneableConfigMap;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.Resource;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpResourceScheduler {
    HttpClient client = HttpClient.newHttpClient();
    KubernetesClient k8sClient;

    public HttpResourceScheduler(KubernetesClient k8sClient) {
        this.k8sClient = k8sClient;
    }

    public void schedule(ConfigSource<HttpSourceConfig> configSource) {


        final var configName = configSource.getTargetConfigMapName();
        final var url = configSource.getSourceConfig().getUrl();
        final var namespace = configSource.getNamespace();

        try {
            final var body = client.send(HttpRequest.newBuilder().uri(URI.create(url)).GET().build(), HttpResponse.BodyHandlers.ofString()).body();

            final Resource<ConfigMap, DoneableConfigMap> configMapResource = k8sClient.configMaps()
                    .inNamespace(namespace)
                    .withName(configName);


            ConfigMap configMap = configMapResource.createOrReplace(new ConfigMapBuilder().
                    withNewMetadata().withName(configName).endMetadata().
                    addToData("config", body).
                    build());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
