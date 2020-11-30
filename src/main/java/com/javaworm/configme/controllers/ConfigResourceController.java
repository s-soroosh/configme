package com.javaworm.configme.controllers;

import com.javaworm.configme.ResourceScheduler;
import com.javaworm.configme.resources.ConfigSourceResource;
import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.api.model.ConfigMapBuilder;
import io.fabric8.kubernetes.api.model.DoneableConfigMap;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.Resource;
import io.javaoperatorsdk.operator.api.Context;
import io.javaoperatorsdk.operator.api.Controller;
import io.javaoperatorsdk.operator.api.ResourceController;
import io.javaoperatorsdk.operator.api.UpdateControl;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Controller(crdName = "configsources.configme.javaworm.com")
public class ConfigResourceController implements ResourceController<ConfigSourceResource> {
    private final KubernetesClient k8sClient;
    private final ResourceScheduler resourceScheduler;

    public ConfigResourceController(ResourceScheduler resourceScheduler, KubernetesClient k8sClient) {
        this.resourceScheduler = resourceScheduler;
        this.k8sClient = k8sClient;
    }

    public boolean deleteResource(
            ConfigSourceResource configSourceResource,
            Context<ConfigSourceResource> context
    ) {
        return false;
    }

    public UpdateControl<ConfigSourceResource> createOrUpdateResource(
            ConfigSourceResource configSourceResource,
            Context<ConfigSourceResource> context
    ) {

        resourceScheduler.schedule(configSourceResource);
        final var configName = configSourceResource.getSpec().getTargetConfigMapName();
        final var url = configSourceResource.getSpec().getSourceConfig().getOrDefault("url", "").toString();
        HttpClient client = HttpClient.newHttpClient();
        try {
            final var body = client.send(HttpRequest.newBuilder().uri(URI.create(url)).GET().build(), HttpResponse.BodyHandlers.ofString()).body();

            final Resource<ConfigMap, DoneableConfigMap> configMapResource = k8sClient.configMaps()
                    .inNamespace(configSourceResource.getMetadata().getNamespace())
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
        return UpdateControl.noUpdate();
    }
}
