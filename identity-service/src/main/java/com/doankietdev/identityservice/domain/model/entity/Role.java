package com.doankietdev.identityservice.domain.model.entity;

import java.util.List;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

@Data
@EqualsAndHashCode(callSuper=true)
@FieldDefaults(level = AccessLevel.PROTECTED)
public class Role extends BaseEntity {
  String name;
  String description;
  List<Permission> permissions;
}
