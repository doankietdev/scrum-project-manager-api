package com.doankietdev.identityservice.domain.model.entity;

import com.doankietdev.identityservice.domain.model.enums.IdentityType;
import com.doankietdev.identityservice.domain.model.enums.UserStatus;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

@Data
@EqualsAndHashCode(callSuper=true)
@FieldDefaults(level = AccessLevel.PROTECTED)
public class User extends BaseEntity {
  String identifier;
  String password;
  IdentityType identityType;
  UserStatus status;

  public boolean isNotVerifiedAccount() {
    return status == UserStatus.PENDING;
  }

  public boolean isActive() {
    return status == UserStatus.ACTIVE;
  }
}
