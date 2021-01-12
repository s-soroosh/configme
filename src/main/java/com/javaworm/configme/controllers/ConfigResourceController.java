package com.javaworm.configme.controllers;

import com.javaworm.configme.RequestContext;
import com.javaworm.configme.ResourceSchedulerManager;
import com.javaworm.configme.events.ConfigSourceEventSource;
import com.javaworm.configme.resources.ConfigSourceResource;
import com.javaworm.configme.resources.ConfigSourceResourceStatus;
import io.javaoperatorsdk.operator.api.Context;
import io.javaoperatorsdk.operator.api.Controller;
import io.javaoperatorsdk.operator.api.DeleteControl;
import io.javaoperatorsdk.operator.api.UpdateControl;
import io.javaoperatorsdk.operator.processing.event.EventSourceManager;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller(crdName = "configsources.configme.javaworm.com")
@RegisterForReflection
public class ConfigResourceController extends BaseResourceController<ConfigSourceResource> {
    private static final Logger LOG = LoggerFactory.getLogger(ConfigResourceController.class);
    private final ResourceSchedulerManager resourceSchedulerManager;
    private ConfigSourceEventSource eventSource = new ConfigSourceEventSource();

    public ConfigResourceController(ResourceSchedulerManager resourceSchedulerManager) {
        this.resourceSchedulerManager = resourceSchedulerManager;
    }

    @Override
    public void init(EventSourceManager eventSourceManager) {
        eventSourceManager.registerEventSource("internal-events", eventSource);
    }

    public DeleteControl deleteResource(
            ConfigSourceResource resource,
            Context<ConfigSourceResource> context
    ) {
        RequestContext<ConfigSourceResource> requestContext = new RequestContext<>(resource, eventSource);
        resourceSchedulerManager.cleanup(requestContext);
        return DeleteControl.DEFAULT_DELETE;
    }

    @Override
    public UpdateControl<ConfigSourceResource> onResourceUpdate(ConfigSourceResource resource, Context<ConfigSourceResource> context) {
        RequestContext<ConfigSourceResource> requestContext = new RequestContext<>(resource, eventSource);
        resourceSchedulerManager.schedule(requestContext);
        return UpdateControl.updateStatusSubResource(resource);
    }

    @Override
    public UpdateControl<ConfigSourceResource> onEvent(ConfigSourceResource resource, Context<ConfigSourceResource> context) {
        context.getEvents().getLatestOfType(ConfigSourceEventSource.AdhocEvent.class)
                .ifPresent(e -> {
                            LOG.info("Updating the status of the resource, new resource: [{}]", e.getMsg());
                            resource.setStatus(new ConfigSourceResourceStatus(e.getMsg()));
                        }
                );

        return UpdateControl.updateStatusSubResource(resource);
    }
}
