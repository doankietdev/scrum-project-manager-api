package com.doankietdev.identityservice.infrastructure.persistence.jpa.entity;

import java.util.Set;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
@Table(name = "roles")
@SQLDelete(sql = "UPDATE `roles` SET deleted_at = now() WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
@DynamicInsert
@DynamicUpdate
public class RoleEntity extends BaseEntity {
  String name;
  String description;

  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable(
    name = "role_permissions",
    joinColumns = @JoinColumn(name = "role_id"),
    inverseJoinColumns = @JoinColumn(name = "permission_id")
  )
  Set<PermissionEntity> permissions;
}
