package com.javaworm.configme;

import com.javaworm.configme.resources.ConfigSourceResource;
import io.fabric8.kubernetes.client.KubernetesClient;

public class ResourceScheduler {
    private final ConfigSourceFactory configSourceFactory;
    private final KubernetesClient k8sClient;

    public ResourceScheduler(ConfigSourceFactory configSourceFactory, KubernetesClient k8sClient) {
        this.configSourceFactory = configSourceFactory;
        this.k8sClient = k8sClient;
    }

    public void schedule(ConfigSourceResource resource) {
        final var configSource = this.configSourceFactory.create(resource);
        if (configSource.getSourceType().equals("http")) {
            new HttpResourceScheduler(k8sClient).schedule(configSource);
        }
    }
}
