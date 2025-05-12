package com.doankietdev.identityservice.domain.repository;

import org.springframework.stereotype.Repository;

import com.doankietdev.identityservice.domain.model.dto.UserCreate;
import com.doankietdev.identityservice.domain.model.dto.UserUpdate;
import com.doankietdev.identityservice.domain.model.entity.User;

@Repository
public interface UserRepository extends BaseRepository<User, UserCreate, UserUpdate> {
  User findByIdentifier(String identifier);
}
