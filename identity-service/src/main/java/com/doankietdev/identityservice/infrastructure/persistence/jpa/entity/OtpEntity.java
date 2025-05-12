package com.doankietdev.identityservice.infrastructure.persistence.jpa.entity;

import java.time.Instant;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.doankietdev.identityservice.domain.model.enums.OtpStatus;
import com.doankietdev.identityservice.domain.model.enums.OtpType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "`otps`")
@DynamicInsert
@DynamicUpdate
@SQLDelete(sql = "UPDATE `otps` SET deleted_at = now() WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
public class OtpEntity extends BaseEntity {
  @ManyToOne(targetEntity = UserEntity.class)
  @JoinColumn(name = "user_id", columnDefinition = "CHAR(36)", nullable = false)
  UserEntity user;

  @Column(columnDefinition = "CHAR(6)", updatable = false)
  String code;

  @Enumerated(EnumType.STRING)
  @Column(updatable = false)
  OtpType type;
  
  @Enumerated(EnumType.STRING)
  OtpStatus status;

  @Column(updatable = false)
  Instant expiresAt;
}
