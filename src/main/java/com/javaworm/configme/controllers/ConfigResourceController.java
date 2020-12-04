package com.javaworm.configme.controllers;

import com.javaworm.configme.ResourceSchedulerManager;
import com.javaworm.configme.resources.ConfigSourceResource;
import com.javaworm.configme.resources.ConfigSourceResourceStatus;
import io.javaoperatorsdk.operator.api.*;
import io.quarkus.runtime.annotations.RegisterForReflection;

@Controller(crdName = "configsources.configme.javaworm.com")
@RegisterForReflection
public class ConfigResourceController implements ResourceController<ConfigSourceResource> {
    private final ResourceSchedulerManager resourceSchedulerManager;

    public ConfigResourceController(ResourceSchedulerManager resourceSchedulerManager) {
        this.resourceSchedulerManager = resourceSchedulerManager;
    }

    public DeleteControl deleteResource(
            ConfigSourceResource configSourceResource,
            Context<ConfigSourceResource> context
    ) {
        return DeleteControl.DEFAULT_DELETE;
    }

    public UpdateControl<ConfigSourceResource> createOrUpdateResource(
            ConfigSourceResource configSourceResource,
            Context<ConfigSourceResource> context
    ) {
        System.out.println("Updating...");
        resourceSchedulerManager.schedule(configSourceResource);
        System.out.println("Successful update!");
        configSourceResource.setStatus(new ConfigSourceResourceStatus("OK"));
        return UpdateControl.updateStatusSubResource(configSourceResource);
    }
}
