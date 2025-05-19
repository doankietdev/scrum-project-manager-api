package com.doankietdev.identityservice.infrastructure.model.criteria;

import java.time.Instant;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginSessionCriteria {
  String userId;
  String ipAddress;
  Instant fromLoginDate;
  Instant toLoginDate;
}
