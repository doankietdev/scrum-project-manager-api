package com.doankietdev.identityservice.domain.model.dto;

import com.doankietdev.identityservice.domain.model.enums.IdentityType;
import com.doankietdev.identityservice.domain.model.enums.UserStatus;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreate {
  String identifier;
  String password;
  IdentityType identityType;
  UserStatus status;
}
