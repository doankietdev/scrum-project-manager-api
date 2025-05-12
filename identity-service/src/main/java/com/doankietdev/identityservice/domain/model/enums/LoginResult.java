package com.doankietdev.identityservice.domain.model.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum LoginResult {
  SUCCESS(1),
  FAILURE(2);

  int code;
}
