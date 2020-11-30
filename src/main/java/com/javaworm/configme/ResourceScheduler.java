package com.javaworm.configme;

import com.javaworm.configme.sources.HttpSourceConfig;

public interface ResourceScheduler {
    void schedule(ConfigSource<HttpSourceConfig> configSource);
}
