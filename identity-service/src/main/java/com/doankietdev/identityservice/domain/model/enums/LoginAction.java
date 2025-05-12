package com.doankietdev.identityservice.domain.model.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum LoginAction {
  LOGIN(1),
  LOGOUT(2),
  FORCED_LOGOUT(3);

  int code;
}
