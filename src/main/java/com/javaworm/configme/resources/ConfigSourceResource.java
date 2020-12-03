package com.javaworm.configme.resources;

import io.fabric8.kubernetes.client.CustomResource;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class ConfigSourceResource extends CustomResource {
    private ConfigSourceResourceSpec spec;
    private ConfigSourceResourceStatus status;

    public ConfigSourceResourceSpec getSpec() {
        return spec;
    }

    public void setSpec(ConfigSourceResourceSpec spec) {
        this.spec = spec;
    }

    public ConfigSourceResourceStatus getStatus() {
        return status;
    }

    public void setStatus(ConfigSourceResourceStatus status) {
        this.status = status;
    }
}
