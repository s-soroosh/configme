package com.javaworm.configme.controllers;

import com.javaworm.configme.resources.ConfigSourceResource;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.javaoperatorsdk.operator.api.Context;
import io.javaoperatorsdk.operator.api.Controller;
import io.javaoperatorsdk.operator.api.ResourceController;
import io.javaoperatorsdk.operator.api.UpdateControl;

@Controller(crdName = "com.javaworm.configme.ConfigResource")
public class ConfigResourceController implements ResourceController<ConfigSourceResource> {
    private final KubernetesClient k8sClient;

    public ConfigResourceController(KubernetesClient k8sClient) {
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
        return null;
    }
}
