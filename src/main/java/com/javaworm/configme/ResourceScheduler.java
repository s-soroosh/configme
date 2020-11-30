package com.javaworm.configme;

import com.javaworm.configme.resources.ConfigSourceResource;

public class ResourceScheduler {
    private final ConfigSourceFactory configSourceFactory;

    public ResourceScheduler(ConfigSourceFactory configSourceFactory) {
        this.configSourceFactory = configSourceFactory;
    }

    public void schedule(ConfigSourceResource resource) {
        final var configSource = this.configSourceFactory.create(resource);
        if (configSource.getSourceType().equals("http")) {
            new HttpResourceScheduler().schedule(configSource);
        }
    }
}
