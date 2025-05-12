package com.doankietdev.identityservice.domain.model.entity;

import java.time.Instant;

import com.doankietdev.identityservice.domain.model.enums.OtpStatus;
import com.doankietdev.identityservice.domain.model.enums.OtpType;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

@Data
@EqualsAndHashCode(callSuper=true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Otp extends BaseEntity {
  User user;
  String code;
  OtpType type;
  OtpStatus status;
  Instant expiresAt;

  public boolean isExpired() {
    Instant now = Instant.now();
    return now.isAfter(expiresAt) || now.equals(expiresAt);
  }

  public boolean isActive() {
    return status == OtpStatus.ACTIVE;
  }
}
