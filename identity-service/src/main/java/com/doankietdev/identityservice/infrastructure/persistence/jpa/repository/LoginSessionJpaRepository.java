package com.doankietdev.identityservice.infrastructure.persistence.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.doankietdev.identityservice.infrastructure.persistence.jpa.entity.LoginSessionEntity;

public interface LoginSessionJpaRepository extends JpaRepository<LoginSessionEntity, String> {
  @Modifying(flushAutomatically = true)
  @Query(value = "DELETE FROM LoginSessionEntity l WHERE l.id = :id")
  void deletePermanentById(@Param("id") String id);

  @Modifying(flushAutomatically = true)
  @Query(value = "UPDATE LoginSessionEntity l SET l.deletedAt = NULL WHERE l.id = :id")
  void restoreById(@Param("id") String id);
}
