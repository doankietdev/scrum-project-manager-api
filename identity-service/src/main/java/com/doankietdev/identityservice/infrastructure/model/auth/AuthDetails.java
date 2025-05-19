package com.doankietdev.identityservice.infrastructure.model.auth;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthDetails {
  String clientIp;
  String userAgent;
}
