package com.javaworm.configme;

import com.javaworm.configme.resources.ConfigSourceResource;

public class ResourceScheduler {
    private final ConfigSourceFactory configSourceFactory;

    public ResourceScheduler(ConfigSourceFactory configSourceFactory) {
        this.configSourceFactory = configSourceFactory;
    }

    public void schedule(ConfigSourceResource resource) {
    }
}
