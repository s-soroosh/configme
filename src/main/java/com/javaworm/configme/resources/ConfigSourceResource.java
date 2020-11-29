package com.javaworm.configme.resources;

import io.fabric8.kubernetes.client.CustomResource;

public class ConfigSourceResource extends CustomResource {
    private ConfigSourceResourceSpec spec;

    public ConfigSourceResourceSpec getSpec() {
        return spec;
    }

    public void setSpec(ConfigSourceResourceSpec spec) {
        this.spec = spec;
    }
}
