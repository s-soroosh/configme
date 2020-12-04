package com.javaworm.configme;

import com.javaworm.configme.resources.ConfigSourceResource;
import io.fabric8.kubernetes.client.KubernetesClient;

import java.util.Map;

public class ResourceSchedulerManager {
    private final ConfigSourceFactory configSourceFactory;

    private final Map<String, ResourceScheduler> sourceTypeSchedulers;

    public ResourceSchedulerManager(ConfigSourceFactory configSourceFactory, KubernetesClient k8sClient, FetchedDataHandler fetchedDataHandler) {
        this.configSourceFactory = configSourceFactory;
        sourceTypeSchedulers = Map.of(
                "http", new HttpResourceScheduler(fetchedDataHandler)
        );
    }

    public void schedule(ConfigSourceResource resource, AdhocEventSource eventSource) {
        final var configSource = this.configSourceFactory.create(resource, eventSource);
        final var sourceType = configSource.getSourceType();
        final var scheduler = sourceTypeSchedulers.get(sourceType);
        if (scheduler == null) {
            eventSource.update(resource, "sorry :(");
            throw new RuntimeException(String.format("No scheduler found for source type [%s]", sourceType));
        }
        scheduler.schedule(configSource);
    }
}
