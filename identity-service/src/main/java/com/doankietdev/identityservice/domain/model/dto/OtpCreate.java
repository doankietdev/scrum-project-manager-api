package com.doankietdev.identityservice.domain.model.dto;

import java.time.Instant;

import com.doankietdev.identityservice.domain.model.entity.User;
import com.doankietdev.identityservice.domain.model.enums.OtpStatus;
import com.doankietdev.identityservice.domain.model.enums.OtpType;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OtpCreate {
  User user;
  String code;
  OtpType type;
  OtpStatus status;
  Instant expiresAt;
}
