package com.doankietdev.identityservice.infrastructure.persistence.jpa.repository;

import java.util.Objects;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import com.doankietdev.identityservice.domain.model.dto.LoginSessionCreate;
import com.doankietdev.identityservice.domain.model.dto.LoginSessionUpdate;
import com.doankietdev.identityservice.domain.model.entity.LoginSession;
import com.doankietdev.identityservice.domain.repository.LoginSessionRepository;
import com.doankietdev.identityservice.infrastructure.persistence.jpa.entity.LoginSessionEntity;
import com.doankietdev.identityservice.infrastructure.persistence.jpa.mapper.LoginSessionMapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Repository
@Slf4j
public class JpaLoginSessionRepositoryImpl implements LoginSessionRepository {
  LoginSessionJpaRepository loginSessionJpaRepository;
  LoginSessionMapper loginSessionMapper;

  @Override
  public LoginSession findById(String id) {
    return loginSessionMapper.toDomain(loginSessionJpaRepository.findById(id).orElse(null));
  }

  @Override
  public LoginSession save(LoginSessionCreate data) {
    LoginSessionEntity loginSessionEntity = loginSessionJpaRepository.save(loginSessionMapper.createToEntity(data));
    return loginSessionMapper.toDomain(loginSessionJpaRepository.findById(loginSessionEntity.getId())
        .orElse(null));
  }

  @Override
  public LoginSession updateById(String id, LoginSessionUpdate updateData) {
    LoginSessionEntity existsLoginSessionEntity = loginSessionJpaRepository.findById(id)
        .orElse(null);
    if (Objects.isNull(existsLoginSessionEntity)) {
      return null;
    }

    BeanUtils.copyProperties(updateData, existsLoginSessionEntity);

    return loginSessionMapper.toDomain(loginSessionJpaRepository.save(existsLoginSessionEntity));
  }

  @Override
  public boolean deleteById(String id) {
    try {
      loginSessionJpaRepository.deleteById(id);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public boolean deletePermanentById(String id) {
    try {
      loginSessionJpaRepository.deletePermanentById(id);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public boolean restoreById(String id) {
    try {
      loginSessionJpaRepository.restoreById(id);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
