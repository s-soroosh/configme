package com.javaworm.configme.controllers;

import io.fabric8.kubernetes.client.CustomResource;
import io.javaoperatorsdk.operator.api.Context;
import io.javaoperatorsdk.operator.api.ResourceController;
import io.javaoperatorsdk.operator.api.UpdateControl;
import io.javaoperatorsdk.operator.processing.event.internal.CustomResourceEvent;

public abstract class BaseResourceController<T extends CustomResource> implements ResourceController<T> {
    @Override
    public final UpdateControl<T> createOrUpdateResource(T resource, Context<T> context) {
        final var customResourceEvent = context.getEvents().getLatestOfType(CustomResourceEvent.class);
        if (customResourceEvent.isPresent()) {
            return onResourceUpdate(resource, context);
        }
        return onEvent(resource, context);
    }

    public abstract UpdateControl<T> onResourceUpdate(T resource, Context<T> context);

    public abstract UpdateControl<T> onEvent(T resource, Context<T> context);
}
