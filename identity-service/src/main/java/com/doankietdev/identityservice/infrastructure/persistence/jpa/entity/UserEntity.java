package com.doankietdev.identityservice.infrastructure.persistence.jpa.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.doankietdev.identityservice.domain.model.enums.IdentityType;
import com.doankietdev.identityservice.domain.model.enums.UserStatus;

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
@Table(name = "`users`")
@SQLDelete(sql = "UPDATE `users` SET deleted_at = now() WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
@DynamicInsert
@DynamicUpdate
public class UserEntity extends BaseEntity {
  String identifier;
  String password;

  @Enumerated(EnumType.STRING)
  @Column(updatable = false)
  IdentityType identityType;

  @Enumerated(EnumType.STRING)
  UserStatus status;
}
