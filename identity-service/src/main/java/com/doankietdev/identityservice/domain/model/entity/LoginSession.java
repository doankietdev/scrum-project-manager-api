package com.doankietdev.identityservice.domain.model.entity;

import java.time.Instant;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

@Data
@EqualsAndHashCode(callSuper=true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginSession extends BaseEntity {
  User user;
  byte[] publicKey;
  String jti;
  String ipAddress;
  String userAgent;
  Instant expiresAt;
}
