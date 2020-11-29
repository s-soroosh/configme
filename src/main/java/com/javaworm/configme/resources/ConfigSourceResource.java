package com.javaworm.configme.resources;

import com.fasterxml.jackson.databind.util.JSONPObject;

public class ConfigSourceResource {
    private String sourceType;
    private JSONPObject sourceConfig;
    private String targetConfigMapName;
}
