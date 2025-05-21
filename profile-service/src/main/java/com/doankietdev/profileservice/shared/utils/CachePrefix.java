package com.doankietdev.profileservice.shared.utils;


public enum CachePrefix {
  LOGIN_SESSION,
  TOKEN_REVOKED,
  AUTHORITIES;

  private final String SERVICE_NAME = "IDENTITY";

  public String removePrefix(String string) {
    return string.substring(string.lastIndexOf(this.name()) + this.name().length() + 1);
  }

  public String getPrefix() {
    return SERVICE_NAME + ":" + this.name() + ":";
  }
}
