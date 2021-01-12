package com.javaworm.configme;

import com.javaworm.configme.events.ConfigSourceEvent;
import com.javaworm.configme.events.K8sEventEmitter;
import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.api.model.ConfigMapBuilder;
import io.fabric8.kubernetes.api.model.DoneableConfigMap;
import io.fabric8.kubernetes.api.model.OwnerReference;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.Resource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;
import java.util.zip.GZIPOutputStream;

public class FetchedDataHandler {
  private final KubernetesClient k8sClient;
  private final K8sEventEmitter k8sEventEmitter;

  public FetchedDataHandler(KubernetesClient k8sClient) {
    this.k8sClient = k8sClient;
    this.k8sEventEmitter = new K8sEventEmitter(k8sClient);
  }

  public void handle(ConfigSource<? extends SourceConfig> configSource, String data)
      throws IOException {
    final var namespace = configSource.getNamespace();
    final var configName = configSource.getTargetConfigMapName();
    final String eventTime =
        LocalDateTime.now(ZoneId.of("UTC"))
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"));

    final Resource<ConfigMap, DoneableConfigMap> configMapResource =
        k8sClient.configMaps().inNamespace(namespace).withName(configName);
    final ConfigSourceEvent event;
    final var existingConfigmap = configMapResource.get();
    final var provideAs =
        Optional.ofNullable(configSource.getResource().getSpec().getProvideAs()).orElse("is");
    System.out.println("provide as " + provideAs);
    if (provideAs.equals("is")) {
      if (existingConfigmap == null) {
        ConfigMap configMap =
            configMapResource.createOrReplace(
                new ConfigMapBuilder()
                    .withNewMetadata()
                    .withName(configName)
                    .withOwnerReferences(
                        new OwnerReference(
                            configSource.getResource().getApiVersion(),
                            true,
                            true,
                            configSource.getResource().getKind(),
                            configSource.getName(),
                            configSource.getUid()))
                    .endMetadata()
                    .addToData("config", data)
                    .addToData("lastUpdate", new Date().toString())
                    .addToData("hash", String.valueOf(data.hashCode()))
                    .build());
        event =
            new ConfigSourceEvent(
                "UPDATE", "dataLoaded", String.format("Configmap %s has been created", configName));
      } else {
        final String existingHash = existingConfigmap.getData().get("hash");
        final String newHash = String.valueOf(data.hashCode());
        if (!newHash.equals(existingHash)) {
          configMapResource.replace(
              new ConfigMapBuilder()
                  .withNewMetadata()
                  .withName(configName)
                  .withOwnerReferences(
                      new OwnerReference(
                          configSource.getResource().getApiVersion(),
                          true,
                          true,
                          configSource.getResource().getKind(),
                          configSource.getName(),
                          configSource.getUid()))
                  .endMetadata()
                  .addToData("config", data)
                  .addToData("lastUpdate", new Date().toString())
                  .addToData("hash", String.valueOf(data.hashCode()))
                  .build());
          event =
              new ConfigSourceEvent(
                  "UPDATE",
                  "dataLoaded",
                  String.format("Configmap %s has been updated", configName));
        } else {
          event =
              new ConfigSourceEvent(
                  "UPDATE",
                  "dataLoaded",
                  String.format("Configmap %s is up to date, left intact", configName));
        }
      }
      this.k8sEventEmitter.emit(configSource.getResource(), event);

    } else if (provideAs.equals("gzip")) {
      if (existingConfigmap == null) {
        byte[] compressedData = compress(data);
        final var encodedCompressedData = Base64.getEncoder().encodeToString(compressedData);
        ConfigMap configMap =
            configMapResource.createOrReplace(
                new ConfigMapBuilder()
                    .withNewMetadata()
                    .withName(configName)
                    .withOwnerReferences(
                        new OwnerReference(
                            configSource.getResource().getApiVersion(),
                            true,
                            true,
                            configSource.getResource().getKind(),
                            configSource.getName(),
                            configSource.getUid()))
                    .endMetadata()
                    .addToBinaryData("config", encodedCompressedData)
                    .addToData("lastUpdate", new Date().toString())
                    .addToData("hash", String.valueOf(data.hashCode()))
                    .build());
        event =
            new ConfigSourceEvent(
                "UPDATE", "dataLoaded", String.format("Configmap %s has been created", configName));
        this.k8sEventEmitter.emit(configSource.getResource(), event);

      } else {
        final String existingHash = existingConfigmap.getData().get("hash");
        final String newHash = String.valueOf(data.hashCode());
        try {
          byte[] compressedData = compress(data);
          final var encodedCompressedData = Base64.getEncoder().encodeToString(compressedData);
          if (!newHash.equals(existingHash)) {
            configMapResource.replace(
                new ConfigMapBuilder()
                    .withNewMetadata()
                    .withName(configName)
                    .withOwnerReferences(
                        new OwnerReference(
                            configSource.getResource().getApiVersion(),
                            true,
                            true,
                            configSource.getResource().getKind(),
                            configSource.getName(),
                            configSource.getUid()))
                    .endMetadata()
                    .addToBinaryData("config", encodedCompressedData)
                    .addToData("lastUpdate", new Date().toString())
                    .addToData("hash", String.valueOf(data.hashCode()))
                    .build());
            event =
                new ConfigSourceEvent(
                    "UPDATE",
                    "dataLoaded",
                    String.format("Configmap %s has been updated", configName));
          } else {
            event =
                new ConfigSourceEvent(
                    "UPDATE",
                    "dataLoaded",
                    String.format("Configmap %s is up to date, left intact", configName));
          }
          this.k8sEventEmitter.emit(configSource.getResource(), event);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private byte[] compress(String data) throws IOException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length());
    GZIPOutputStream gzip = new GZIPOutputStream(bos);
    gzip.write(data.getBytes());
    gzip.close();
    byte[] compressed = bos.toByteArray();
    bos.close();
    return compressed;
  }
}
