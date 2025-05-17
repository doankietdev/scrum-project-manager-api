package com.doankietdev.identityservice.infrastructure.persistence.jpa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.doankietdev.identityservice.domain.model.enums.OtpType;
import com.doankietdev.identityservice.infrastructure.persistence.jpa.entity.OtpEntity;

public interface OtpJpaStore extends JpaRepository<OtpEntity, String> {
  Optional<OtpEntity> findByUserIdAndCodeAndType(String userId, String code, OtpType type);

  @Modifying(flushAutomatically = true)
  @Query(value = "DELETE FROM OtpEntity l WHERE l.id = :id")
  void deletePermanentById(@Param("id") String id);
  
  @Modifying(flushAutomatically = true)
  @Query(value = "UPDATE OtpEntity o SET o.deletedAt = NULL WHERE o.id = :id")
  void restoreById(@Param("id") String id);
}
