package com.javaworm.configme.controllers;

import com.javaworm.configme.AdhocEventSource;
import com.javaworm.configme.RequestContext;
import com.javaworm.configme.ResourceSchedulerManager;
import com.javaworm.configme.resources.ConfigSourceResource;
import com.javaworm.configme.resources.ConfigSourceResourceStatus;
import io.javaoperatorsdk.operator.api.*;
import io.javaoperatorsdk.operator.processing.event.EventSourceManager;
import io.javaoperatorsdk.operator.processing.event.internal.CustomResourceEvent;
import io.quarkus.runtime.annotations.RegisterForReflection;

@Controller(crdName = "configsources.configme.javaworm.com")
@RegisterForReflection
public class ConfigResourceController implements ResourceController<ConfigSourceResource> {
    private final ResourceSchedulerManager resourceSchedulerManager;
    private AdhocEventSource eventSource = new AdhocEventSource();

    public ConfigResourceController(ResourceSchedulerManager resourceSchedulerManager) {
        this.resourceSchedulerManager = resourceSchedulerManager;
    }

    @Override
    public void init(EventSourceManager eventSourceManager) {
        eventSourceManager.registerEventSource("internal-events", eventSource);
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
        final var adhocEvent = context.getEvents().getLatestOfType(AdhocEventSource.AdhocEvent.class);
        final var customResourceEvent = context.getEvents().getLatestOfType(CustomResourceEvent.class);
        if (adhocEvent.isPresent()) {
            configSourceResource.setStatus(new ConfigSourceResourceStatus(adhocEvent.get().getMsg()));
        }
        if (customResourceEvent.isPresent()) {
            RequestContext<ConfigSourceResource> requestContext = new RequestContext<>(configSourceResource, eventSource);
            resourceSchedulerManager.schedule(requestContext);
        }
        return UpdateControl.updateStatusSubResource(configSourceResource);
    }
}
