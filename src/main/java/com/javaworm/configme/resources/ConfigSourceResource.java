package com.javaworm.configme.resources;

import com.fasterxml.jackson.databind.util.JSONPObject;
import io.fabric8.kubernetes.client.CustomResource;

public class ConfigSourceResource extends CustomResource {
    private String sourceType;
    private JSONPObject sourceConfig;
    private String targetConfigMapName;
}
