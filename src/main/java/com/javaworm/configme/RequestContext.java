package com.javaworm.configme;

import com.javaworm.configme.events.ConfigSourceEventSource;
import io.fabric8.kubernetes.client.CustomResource;

public class RequestContext<T extends CustomResource> {
    private T resource;
    private ConfigSourceEventSource eventSource;

    public RequestContext(T resource, ConfigSourceEventSource eventSource) {
        this.resource = resource;
        this.eventSource = eventSource;
    }

    public void emit(String message) {
        this.eventSource.update(resource, message);
    }

    public T getResource() {
        return resource;
    }

//    public AdhocEventSource getEventSource() {
//        return eventSource;
//    }
}
