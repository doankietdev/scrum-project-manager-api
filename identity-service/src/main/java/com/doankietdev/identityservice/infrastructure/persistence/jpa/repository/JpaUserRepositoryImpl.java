package com.doankietdev.identityservice.infrastructure.persistence.jpa.repository;

import java.util.Objects;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import com.doankietdev.identityservice.domain.model.dto.UserCreate;
import com.doankietdev.identityservice.domain.model.dto.UserUpdate;
import com.doankietdev.identityservice.domain.model.entity.User;
import com.doankietdev.identityservice.domain.repository.UserRepository;
import com.doankietdev.identityservice.infrastructure.persistence.jpa.entity.UserEntity;
import com.doankietdev.identityservice.infrastructure.persistence.jpa.mapper.UserMapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Repository
@Slf4j
public class JpaUserRepositoryImpl implements UserRepository {
  UserJpaRepository userJpaRepository;
  UserMapper userMapper;

  @Override
  public User findById(String id) {
    return userMapper.toDomain(userJpaRepository.findById(id).orElse(null));
  }

  @Override
  public User save(UserCreate data) {
    UserEntity userEntity = userJpaRepository.save(userMapper.createToEntity(data));
    return userMapper.toDomain(userJpaRepository.findById(userEntity.getId())
        .orElse(null));
  }

  @Override
  public User updateById(String id, UserUpdate updateData) {
    UserEntity existsUserEntity = userJpaRepository.findById(id)
        .orElse(null);
    if (Objects.isNull(existsUserEntity)) {
      return null;
    }

    BeanUtils.copyProperties(updateData, existsUserEntity);

    return userMapper.toDomain(userJpaRepository.save(existsUserEntity));
  }

  @Override
  public boolean deleteById(String id) {
    try {
      userJpaRepository.deleteById(id);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public boolean deletePermanentById(String id) {
    try {
      userJpaRepository.deletePermanentById(id);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public boolean restoreById(String id) {
     try {
      userJpaRepository.restoreById(id);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public User findByIdentifier(String identifier) {
    return userMapper.toDomain(userJpaRepository.findByIdentifier(identifier)
        .orElse(null));
  }
}
