package com.javaworm.configme.sources;

public class BearerAuthenticationConfig {
  private String tokenType;
  private String tokenTypeKey;
  private String tokenSecretKey;

  public String getTokenType() {
    return tokenType;
  }

  public String getTokenTypeKey() {
    return tokenTypeKey;
  }

  public String getTokenSecretKey() {
    return tokenSecretKey;
  }
}
