package com.javaworm.configme.events;

import io.fabric8.kubernetes.api.model.Event;
import io.fabric8.kubernetes.api.model.EventBuilder;

public abstract class ConfigSourceEvent {
  private final String eventType;
  private final String reason;
  private final String message;

  public ConfigSourceEvent(String eventType, String reason, String message) {
    this.eventType = eventType;
    this.reason = reason;
    this.message = message;
  }

  public Event toK8sEvent() {
    final var eventId = (reason + "." + message).hashCode();
    return new EventBuilder()
        .withNewMetadata()
        .withName("configme." + eventId)
        .endMetadata()
        .withType(this.eventType)
        .withReason(this.reason)
        .withMessage(this.message)
        .build();
  }
}
