package com.doankietdev.identityservice.application.model.dto;

import java.time.Instant;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthKeyToken {
  byte[] publicKey;
  String jti;
  Instant expiresAt;
  String accessToken;
  String refreshToken;
}
