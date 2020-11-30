package com.javaworm.configme.controllers;

import com.javaworm.configme.ResourceScheduler;
import com.javaworm.configme.resources.ConfigSourceResource;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.javaoperatorsdk.operator.api.Context;
import io.javaoperatorsdk.operator.api.Controller;
import io.javaoperatorsdk.operator.api.ResourceController;
import io.javaoperatorsdk.operator.api.UpdateControl;

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
        return UpdateControl.noUpdate();
    }
}
