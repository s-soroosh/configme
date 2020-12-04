package com.javaworm.configme;

import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.api.model.ConfigMapBuilder;
import io.fabric8.kubernetes.api.model.DoneableConfigMap;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.Resource;

import java.util.Date;

public class FetchedDataHandler {
    private final KubernetesClient k8sClient;

    public FetchedDataHandler(KubernetesClient k8sClient) {
        this.k8sClient = k8sClient;
    }

    public void handle(ConfigSource<? extends SourceConfig> configSource, String data) {
        final var namespace = configSource.getNamespace();
        final var configName = configSource.getTargetConfigMapName();
        final Resource<ConfigMap, DoneableConfigMap> configMapResource = k8sClient.configMaps()
                .inNamespace(namespace)
                .withName(configName);

        final var existingConfigmap = configMapResource.get();
        if (existingConfigmap == null) {
            ConfigMap configMap = configMapResource.createOrReplace(new ConfigMapBuilder().
                    withNewMetadata()
                    .withName(configName)
                    .endMetadata()
                    .addToData("config", data)
                    .addToData("lastUpdate", new Date().toString())
                    .addToData("hash", String.valueOf(data.hashCode()))

                    .build());
        } else {
            final String existingHash = existingConfigmap.getData().get("hash");
            final String newHash = String.valueOf(data.hashCode());
            if (!newHash.equals(existingHash)) {
                configMapResource.replace(new ConfigMapBuilder().
                        withNewMetadata()
                        .withName(configName)
                        .endMetadata()
                        .addToData("config", data)
                        .addToData("lastUpdate", new Date().toString())
                        .addToData("hash", String.valueOf(data.hashCode()))
                        .build());
            }
        }
    }
}
