package com.doankietdev.identityservice.infrastructure.persistence.jpa.entity;

import java.time.Instant;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "`login_session`")
@SQLDelete(sql = "UPDATE `login_session` SET deleted_at = now() WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
@DynamicInsert
@DynamicUpdate
public class LoginSessionEntity extends BaseEntity {
  @ManyToOne(targetEntity = UserEntity.class)
  @JoinColumn(name = "user_id", columnDefinition = "CHAR(36)", nullable = false)
  UserEntity user;

  @Column(columnDefinition = "BLOB")
  byte[] publicKey;

  @Column(columnDefinition = "CHAR(36)")
  String jti;

  @Column(columnDefinition = "VARCHAR(45)")
  String ipAddress;

  String userAgent;

  Instant expiresAt;
}
