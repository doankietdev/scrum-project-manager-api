package com.doankietdev.identityservice.infrastructure.persistence.jpa.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.doankietdev.identityservice.domain.model.enums.PermissionType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "`permissions`")
@SQLDelete(sql = "UPDATE `permissions` SET deleted_at = now() WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
@DynamicInsert
@DynamicUpdate
public class PermissionEntity extends BaseEntity {
  String name;
  String description;

  @Enumerated(EnumType.STRING)
  @Column(updatable = false)
  PermissionType type;
}
