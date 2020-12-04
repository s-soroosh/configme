package com.javaworm.configme;

import io.fabric8.kubernetes.client.CustomResource;
import io.javaoperatorsdk.operator.processing.KubernetesResourceUtils;
import io.javaoperatorsdk.operator.processing.event.AbstractEventSource;
import io.javaoperatorsdk.operator.processing.event.Event;
import io.javaoperatorsdk.operator.processing.event.EventSource;

public class AdhocEventSource extends AbstractEventSource {
    public void update(CustomResource resource, String msg) {
        System.out.println("event for resource " + resource.getMetadata().getUid());
        this.eventHandler.handleEvent(new AdhocEvent(resource, msg));
    }

    public class AdhocEvent implements Event {
        private final CustomResource customResource;
        private String msg;

        public AdhocEvent(CustomResource customResource, String msg) {
            this.customResource = customResource;
            this.msg = msg;
        }

        @Override
        public String getRelatedCustomResourceUid() {
            return KubernetesResourceUtils.getUID(customResource);
        }

        @Override
        public EventSource getEventSource() {
            return AdhocEventSource.this;
        }

        public String getMsg() {
            return msg;
        }
    }
}
