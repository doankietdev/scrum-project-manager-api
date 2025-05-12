package com.doankietdev.identityservice.domain.model.dto;

import java.time.Instant;

import com.doankietdev.identityservice.domain.model.entity.User;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginSessionCreate {
  User user;
  byte[] publicKey;
  String jti;
  String ipAddress;
  String userAgent;
  Instant expiresAt;
}
