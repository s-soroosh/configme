package com.javaworm.configme;

import com.javaworm.configme.resources.ConfigSourceResource;
import com.javaworm.configme.sources.HttpSourceConfig;

import java.util.concurrent.CompletableFuture;

public interface ResourceScheduler {
    void schedule(ConfigSource<HttpSourceConfig> configSource);

    void cancel(RequestContext<ConfigSourceResource> context);
}
