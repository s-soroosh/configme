package com.javaworm.configme;

import com.javaworm.configme.resources.ConfigSourceResource;
import io.fabric8.kubernetes.client.KubernetesClient;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ResourceSchedulerManager {
    private final ConfigSourceFactory configSourceFactory;

    private final Map<String, ResourceScheduler> sourceTypeSchedulers;

    public ResourceSchedulerManager(ConfigSourceFactory configSourceFactory, KubernetesClient k8sClient) {
        this.configSourceFactory = configSourceFactory;
        sourceTypeSchedulers = Map.of(
                "http", new HttpResourceScheduler(k8sClient)
        );
    }

    public void schedule(ConfigSourceResource resource) {
        final var configSource = this.configSourceFactory.create(resource);
        final var sourceType = configSource.getSourceType();
        final var scheduler = sourceTypeSchedulers.get(sourceType);
        if (scheduler == null) {
            throw new RuntimeException("No scheduler found for source type [%s]".formatted(sourceType));
        }
        final var schedule = scheduler.schedule(configSource);

        if (resource.getSpec().isBlocking()) {
            try {
                schedule.get(resource.getSpec().getBlockingTimeoutSeconds(),
                        TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }
    }
}
