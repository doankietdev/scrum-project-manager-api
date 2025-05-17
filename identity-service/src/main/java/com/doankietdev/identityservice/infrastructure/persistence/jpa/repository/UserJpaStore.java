package com.doankietdev.identityservice.infrastructure.persistence.jpa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.doankietdev.identityservice.infrastructure.persistence.jpa.entity.UserEntity;

public interface UserJpaStore extends JpaRepository<UserEntity, String> {
  Optional<UserEntity> findByIdentifier(String identifier);

  @Modifying(flushAutomatically = true)
  @Query(value = "DELETE FROM UserEntity u WHERE u.id = :id")
  void deletePermanentById(@Param("id") String id);

  @Modifying(flushAutomatically = true)
  @Query(value = "UPDATE UserEntity u SET u.deletedAt = NULL WHERE u.id = :id")
  void restoreById(@Param("id") String id);
}
