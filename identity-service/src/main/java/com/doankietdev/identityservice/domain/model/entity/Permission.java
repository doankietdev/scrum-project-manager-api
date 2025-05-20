package com.doankietdev.identityservice.domain.model.entity;

import com.doankietdev.identityservice.domain.model.enums.PermissionType;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

@Data
@EqualsAndHashCode(callSuper=true)
@FieldDefaults(level = AccessLevel.PROTECTED)
public class Permission extends BaseEntity {
  String name;
  String description;
  PermissionType type;
}
