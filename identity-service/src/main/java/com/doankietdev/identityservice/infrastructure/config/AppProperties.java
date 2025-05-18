package com.doankietdev.identityservice.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {
  @Value("${spring.profiles.active}")
  String envName;
  
  Auth auth = new Auth();

  Cache cache = new Cache();

  @Getter
  @Setter
  @FieldDefaults(level = AccessLevel.PRIVATE)
  public static class Auth {
    Long emailVerificationOtpExpirationTime = 300L;
    Integer emailVerificationOtpLength = 6;
    Long accessTokenExpirationTime = 3600L;
    Long refreshTokenExpirationTime = 604800L;
  }

  @Getter
  @Setter
  @FieldDefaults(level = AccessLevel.PRIVATE)
  public static class Cache {
    Long expirationTime = 7200L;
  }
}
