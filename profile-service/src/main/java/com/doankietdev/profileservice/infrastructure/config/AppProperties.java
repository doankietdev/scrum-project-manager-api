package com.doankietdev.profileservice.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
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

  Cache cache = new Cache();

  @Getter
  @Setter
  @FieldDefaults(level = AccessLevel.PRIVATE)
  public static class Cache {
    Long expirationTime = 7200L;
  }
}
