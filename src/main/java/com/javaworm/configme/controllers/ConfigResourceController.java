package com.javaworm.configme.controllers;

import com.javaworm.configme.resources.ConfigSourceResource;
import io.javaoperatorsdk.operator.api.Context;
import io.javaoperatorsdk.operator.api.ResourceController;
import io.javaoperatorsdk.operator.api.UpdateControl;

public class ConfigResourceController implements ResourceController<ConfigSourceResource> {
    public boolean deleteResource(ConfigSourceResource configSourceResource, Context<ConfigSourceResource> context) {
        return false;
    }

    public UpdateControl<ConfigSourceResource> createOrUpdateResource(ConfigSourceResource configSourceResource, Context<ConfigSourceResource> context) {
        return null;
    }
}
