package com.doankietdev.identityservice.domain.repository;

import org.springframework.stereotype.Repository;

import com.doankietdev.identityservice.domain.model.dto.OtpCreate;
import com.doankietdev.identityservice.domain.model.dto.OtpUpdate;
import com.doankietdev.identityservice.domain.model.entity.Otp;
import com.doankietdev.identityservice.domain.model.enums.OtpType;

@Repository
public interface OtpRepository extends BaseRepository<Otp, OtpCreate, OtpUpdate> {
  Otp findByUserIdAndCodeAndType(String userId, String code, OtpType type);
}
