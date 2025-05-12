package com.doankietdev.identityservice.domain.model.dto;

import java.time.Instant;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginSessionUpdate {
  byte[] publicKey;
  String jti;
  String ipAddress;
  String userAgent;
  Instant expiresAt;
}
