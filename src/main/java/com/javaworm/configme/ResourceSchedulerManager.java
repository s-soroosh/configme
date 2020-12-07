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

    public void schedule(RequestContext<ConfigSourceResource> context) {
        cancelExistingScheduleFor(context);
        final var configSource = this.configSourceFactory.create(context);
        final var sourceType = configSource.getSourceType();
        final var scheduler = sourceTypeSchedulers.get(sourceType);
        if (scheduler == null) {
            final var errorMessage = String.format("No scheduler found for source type [%s]", sourceType);
            context.emit(errorMessage);
            throw new RuntimeException(errorMessage);
        }
        scheduler.schedule(configSource);
    }

    public void cleanup(RequestContext<ConfigSourceResource> context) {
        cancelExistingScheduleFor(context);
    }

    private void cancelExistingScheduleFor(RequestContext<ConfigSourceResource> context) {
        sourceTypeSchedulers.values().forEach(scheduler -> scheduler.cancel(context));
    }
}
