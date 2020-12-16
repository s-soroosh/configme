package com.javaworm.configme.events;

import io.fabric8.kubernetes.api.model.DoneableEvent;
import io.fabric8.kubernetes.api.model.Event;
import io.fabric8.kubernetes.api.model.EventList;
import io.fabric8.kubernetes.api.model.ObjectReference;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.Resource;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class K8sEventEmitter {

    private final MixedOperation<Event, EventList, DoneableEvent, Resource<Event, DoneableEvent>> events;

    public K8sEventEmitter(KubernetesClient k8sClient) {
        events = k8sClient.v1().events();
    }

    public Event emit(CustomResource resource, Event event) {
        final String eventTime = LocalDateTime.now(ZoneId.of("UTC")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"));
        final var namespace = resource.getMetadata().getNamespace();
        final var eventName = event.getMetadata().getName();
        final var existingEvent = events.inNamespace(namespace).withName(eventName).get();

        if (existingEvent != null) {
            event.setFirstTimestamp(existingEvent.getFirstTimestamp());
            event.setCount(existingEvent.getCount() == null ? 1 : existingEvent.getCount() + 1);
        } else {
            event.setFirstTimestamp(eventTime);
            event.setCount(1);
        }
        event.setLastTimestamp(eventTime);
        this.setInvolvedObject(resource, event);
        return events.inNamespace(namespace).createOrReplace(event);
    }

    private Event setInvolvedObject(CustomResource resource, Event event) {
        final var metadata = resource.getMetadata();
        event.setInvolvedObject(
                new ObjectReference(
                        resource.getApiVersion(),
                        null,
                        resource.getKind(),
                        metadata.getName(),
                        metadata.getNamespace(),
                        metadata.getResourceVersion(),
                        metadata.getUid())
        );
        return event;

    }

}
