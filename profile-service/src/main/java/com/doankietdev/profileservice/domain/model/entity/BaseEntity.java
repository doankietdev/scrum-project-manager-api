package com.doankietdev.profileservice.domain.model.entity;

import java.time.Instant;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class BaseEntity {
  String id;
  Instant createdAt;
  Instant updatedAt;
  Instant deletedAt;
}
