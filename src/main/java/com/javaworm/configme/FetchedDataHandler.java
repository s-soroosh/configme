package com.javaworm.configme;

import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.Resource;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;

public class FetchedDataHandler {
    private final KubernetesClient k8sClient;


    public FetchedDataHandler(KubernetesClient k8sClient) {
        this.k8sClient = k8sClient;
    }

    public void handle(ConfigSource<? extends SourceConfig> configSource, String data) {
        final var namespace = configSource.getNamespace();
        final var configName = configSource.getTargetConfigMapName();
        final String eventTime = LocalDateTime.now(ZoneId.of("UTC")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"));
        final var event = new EventBuilder()
                .withInvolvedObject(new ObjectReference(
                        configSource.getResource().getApiVersion(),
                        null,
                        configSource.getResource().getKind(),
                        configSource.getName(),
                        configSource.getNamespace(),
                        null,
                        configSource.getUid()))
                .withNewMetadata().withName("X3-EVENT").endMetadata()
                .withCount(10)
                .withFirstTimestamp(eventTime)
                .withLastTimestamp(eventTime)
//                .withEventTime(:)
                .withAction("X1")
                .withType("T1")
                .withReason("R1")
                .withMessage("a test message").build();


//        event.setInvolvedObject();
        k8sClient.v1().events().inNamespace(configSource.getNamespace()).createOrReplace(event);
        final Resource<ConfigMap, DoneableConfigMap> configMapResource = k8sClient.configMaps()
                .inNamespace(namespace)
                .withName(configName);

        final var existingConfigmap = configMapResource.get();
        if (existingConfigmap == null) {
            ConfigMap configMap = configMapResource.createOrReplace(new ConfigMapBuilder().
                    withNewMetadata()
                    .withName(configName)
                    .withOwnerReferences(new OwnerReference(
                            configSource.getResource().getApiVersion(),
                            true,
                            true,
                            configSource.getResource().getKind(),
                            configSource.getName(),
                            configSource.getUid())
                    )
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
                        .withOwnerReferences(new OwnerReference(
                                configSource.getResource().getApiVersion(),
                                true,
                                true,
                                configSource.getResource().getKind(),
                                configSource.getName(),
                                configSource.getUid())
                        )
                        .endMetadata()
                        .addToData("config", data)
                        .addToData("lastUpdate", new Date().toString())
                        .addToData("hash", String.valueOf(data.hashCode()))
                        .build());
            }
        }
    }
}
