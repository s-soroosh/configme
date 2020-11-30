package com.javaworm.configme.controllers;

import com.javaworm.configme.ResourceSchedulerManager;
import com.javaworm.configme.resources.ConfigSourceResource;
import io.javaoperatorsdk.operator.api.Context;
import io.javaoperatorsdk.operator.api.Controller;
import io.javaoperatorsdk.operator.api.ResourceController;
import io.javaoperatorsdk.operator.api.UpdateControl;

@Controller(crdName = "configsources.configme.javaworm.com")
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
        return UpdateControl.noUpdate();
    }
}
