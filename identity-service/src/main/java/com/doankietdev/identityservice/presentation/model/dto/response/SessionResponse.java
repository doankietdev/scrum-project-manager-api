package com.doankietdev.identityservice.presentation.model.dto.response;

import java.time.Instant;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SessionResponse {
  String ipAddress;
  Instant loginAt;
}
