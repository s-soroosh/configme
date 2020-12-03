package com.javaworm.configme.resources;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class ConfigSourceResourceStatus {

    private ConfigSourceResourceStatus() {

    }

    public ConfigSourceResourceStatus(String description) {
        this.description = description;
    }

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
