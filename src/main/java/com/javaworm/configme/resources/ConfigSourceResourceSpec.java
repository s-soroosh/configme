package com.javaworm.configme.resources;

import io.quarkus.runtime.annotations.RegisterForReflection;
import java.util.Map;

@RegisterForReflection
public class ConfigSourceResourceSpec {
  private String sourceType;
  private Map sourceConfig;
  private String targetConfigMapName;

  private String provideAs;

  public String getSourceType() {
    return sourceType;
  }

  public Map getSourceConfig() {
    return sourceConfig;
  }

  public String getTargetConfigMapName() {
    return targetConfigMapName;
  }

  public String getProvideAs() {
    return provideAs;
  }
}
