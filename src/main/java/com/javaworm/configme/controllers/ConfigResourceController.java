package com.javaworm.configme.controllers;

import com.javaworm.configme.ResourceSchedulerManager;
import com.javaworm.configme.resources.ConfigSourceResource;
import com.javaworm.configme.resources.ConfigSourceResourceStatus;
import io.javaoperatorsdk.operator.api.Context;
import io.javaoperatorsdk.operator.api.Controller;
import io.javaoperatorsdk.operator.api.ResourceController;
import io.javaoperatorsdk.operator.api.UpdateControl;
import io.quarkus.runtime.annotations.RegisterForReflection;

@Controller(crdName = "configsources.configme.javaworm.com")
@RegisterForReflection
public class ConfigResourceController implements ResourceController<ConfigSourceResource> {
    private final ResourceSchedulerManager resourceSchedulerManager;

    public ConfigResourceController(ResourceSchedulerManager resourceSchedulerManager) {
        this.resourceSchedulerManager = resourceSchedulerManager;
    }

    public boolean deleteResource(
            ConfigSourceResource configSourceResource,
            Context<ConfigSourceResource> context
    ) {
        return true;
    }

    public UpdateControl<ConfigSourceResource> createOrUpdateResource(
            ConfigSourceResource configSourceResource,
            Context<ConfigSourceResource> context
    ) {
        resourceSchedulerManager.schedule(configSourceResource);
        System.out.println("Successful update!");
        configSourceResource.setStatus(new ConfigSourceResourceStatus("OK"));
        return UpdateControl.updateStatusSubResource(configSourceResource);
    }
}
