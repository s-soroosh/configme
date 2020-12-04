package com.javaworm.configme;

import io.fabric8.kubernetes.client.CustomResource;

public class RequestContext<T extends CustomResource> {
    private T resource;
    private AdhocEventSource eventSource;

    public RequestContext(T resource, AdhocEventSource eventSource) {
        this.resource = resource;
        this.eventSource = eventSource;
    }

    public T getResource() {
        return resource;
    }

    public AdhocEventSource getEventSource() {
        return eventSource;
    }
}
