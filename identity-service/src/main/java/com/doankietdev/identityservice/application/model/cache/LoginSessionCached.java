package com.doankietdev.identityservice.application.model.cache;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginSessionCached {
  String userId;
  String jti;
  byte[] publicKey;
}
