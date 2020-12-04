package com.javaworm.configme.controllers;

import com.javaworm.configme.AdhocEventSource;
import com.javaworm.configme.ResourceSchedulerManager;
import com.javaworm.configme.resources.ConfigSourceResource;
import com.javaworm.configme.resources.ConfigSourceResourceStatus;
import io.javaoperatorsdk.operator.api.*;
import io.javaoperatorsdk.operator.processing.event.EventSourceManager;
import io.quarkus.runtime.annotations.RegisterForReflection;

@Controller(crdName = "configsources.configme.javaworm.com")
@RegisterForReflection
public class ConfigResourceController implements ResourceController<ConfigSourceResource> {
    private final ResourceSchedulerManager resourceSchedulerManager;
    private AdhocEventSource eventSource;

    public ConfigResourceController(ResourceSchedulerManager resourceSchedulerManager) {
        this.resourceSchedulerManager = resourceSchedulerManager;
    }

    @Override
    public void init(EventSourceManager eventSourceManager) {
        eventSourceManager.registerEventSource("internal-events", new AdhocEventSource());
        eventSource = (AdhocEventSource) eventSourceManager.getRegisteredEventSources().get("internal-events");
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
        if (adhocEvent.isPresent()) {
            configSourceResource.setStatus(new ConfigSourceResourceStatus(adhocEvent.get().getMsg()));
            return UpdateControl.updateStatusSubResource(configSourceResource);
        }
        resourceSchedulerManager.schedule(configSourceResource, eventSource);
        return UpdateControl.updateStatusSubResource(configSourceResource);
    }
}
