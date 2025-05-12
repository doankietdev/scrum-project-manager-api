package com.doankietdev.identityservice.infrastructure.persistence.jpa.repository;

import java.util.Objects;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import com.doankietdev.identityservice.domain.model.dto.OtpCreate;
import com.doankietdev.identityservice.domain.model.dto.OtpUpdate;
import com.doankietdev.identityservice.domain.model.entity.Otp;
import com.doankietdev.identityservice.domain.model.enums.OtpType;
import com.doankietdev.identityservice.domain.repository.OtpRepository;
import com.doankietdev.identityservice.infrastructure.persistence.jpa.entity.OtpEntity;
import com.doankietdev.identityservice.infrastructure.persistence.jpa.mapper.OtpMapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Repository
public class JpaOtpRepositoryImpl implements OtpRepository {
  OtpJpaRepository otpJpaRepository;
  OtpMapper otpMapper;

  @Override
  public Otp findById(String id) {
    return otpMapper.toDomain(otpJpaRepository.findById(id).orElse(null));
  }

  @Override
  public Otp save(OtpCreate data) {
    return otpMapper.toDomain(otpJpaRepository.save(otpMapper.createToEntity(data)));
  }

  @Override
  public Otp updateById(String id, OtpUpdate updateData) {
    OtpEntity existsOtpEntity = otpJpaRepository.findById(id)
        .orElse(null);
    if (Objects.isNull(existsOtpEntity)) {
      return null;
    }

    BeanUtils.copyProperties(updateData, existsOtpEntity);

    return otpMapper.toDomain(otpJpaRepository.save(existsOtpEntity));
  }

  @Override
  public boolean deleteById(String id) {
    try {
      otpJpaRepository.deleteById(id);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public boolean deletePermanentById(String id) {
    try {
      otpJpaRepository.deletePermanentById(id);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public boolean restoreById(String id) {
    try {
      otpJpaRepository.restoreById(id);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public Otp findByUserIdAndCodeAndType(String userId, String code, OtpType type) {
    return otpMapper.toDomain(otpJpaRepository.findByUserIdAndCodeAndType(userId, code, type)
        .orElse(null));
  }
}
