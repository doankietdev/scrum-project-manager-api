package com.doankietdev.identityservice.infrastructure.persistence.jpa.repository;

import java.util.Objects;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import com.doankietdev.identityservice.domain.model.dto.OtpCreate;
import com.doankietdev.identityservice.domain.model.dto.OtpUpdate;
import com.doankietdev.identityservice.domain.model.entity.Otp;
import com.doankietdev.identityservice.domain.model.enums.OtpType;
import com.doankietdev.identityservice.domain.repository.OtpRepository;
import com.doankietdev.identityservice.infrastructure.mapper.jpa.OtpJpaMapper;
import com.doankietdev.identityservice.infrastructure.persistence.jpa.entity.OtpEntity;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Repository
public class JpaOtpRepository implements OtpRepository {
  OtpJpaStore otpJpaStore;
  OtpJpaMapper otpJpaMapper;

  @Override
  public Otp findById(String id) {
    return otpJpaMapper.toDomain(otpJpaStore.findById(id).orElse(null));
  }

  @Override
  public Otp save(OtpCreate data) {
    return otpJpaMapper.toDomain(otpJpaStore.save(otpJpaMapper.createToEntity(data)));
  }

  @Override
  public Otp updateById(String id, OtpUpdate updateData) {
    OtpEntity existsOtpEntity = otpJpaStore.findById(id)
        .orElse(null);
    if (Objects.isNull(existsOtpEntity)) {
      return null;
    }

    BeanUtils.copyProperties(updateData, existsOtpEntity);

    return otpJpaMapper.toDomain(otpJpaStore.save(existsOtpEntity));
  }

  @Override
  public boolean deleteById(String id) {
    try {
      otpJpaStore.deleteById(id);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public boolean deletePermanentById(String id) {
    try {
      otpJpaStore.deletePermanentById(id);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public boolean restoreById(String id) {
    try {
      otpJpaStore.restoreById(id);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public Otp findByUserIdAndCodeAndType(String userId, String code, OtpType type) {
    return otpJpaMapper.toDomain(otpJpaStore.findByUserIdAndCodeAndType(userId, code, type)
        .orElse(null));
  }
}
