package com.javaworm.configme.sources;

public class BearerAuthenticationConfig {
  private String tokenType;
  private String secretName;
  private String tokenSecretKey;

  public BearerAuthenticationConfig(String tokenType, String secretName,
      String tokenSecretKey) {
    this.tokenType = tokenType;
    this.secretName = secretName;
    this.tokenSecretKey = tokenSecretKey;
  }

  public String getTokenType() {
    return tokenType;
  }

  public String getSecretName() {
    return secretName;
  }

  public String getTokenSecretKey() {
    return tokenSecretKey;
  }
}
