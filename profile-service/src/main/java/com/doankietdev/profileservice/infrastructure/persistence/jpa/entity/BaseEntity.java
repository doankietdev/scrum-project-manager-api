package com.doankietdev.profileservice.infrastructure.persistence.jpa.entity;

import java.io.Serializable;
import java.time.Instant;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PROTECTED)
@MappedSuperclass
public abstract class BaseEntity implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(columnDefinition = "CHAR(36)")
  String id;

  @Column(updatable = false)
  Instant createdAt;

  @Column(updatable = false)
  Instant updatedAt;

  @Nullable
  Instant deletedAt;
}
