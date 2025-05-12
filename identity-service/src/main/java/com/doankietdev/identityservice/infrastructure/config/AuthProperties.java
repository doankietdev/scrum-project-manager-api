package com.doankietdev.identityservice.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Configuration
@ConfigurationProperties(prefix = "app.auth")
public class AuthProperties {
  long emailVerificationOtpTime;
  int emailVerificationOtpLength;
  long accessTokenTime;
  long refreshTokenTime;
}
