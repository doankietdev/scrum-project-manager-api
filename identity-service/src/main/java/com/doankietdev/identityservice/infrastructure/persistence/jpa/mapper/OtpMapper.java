package com.doankietdev.identityservice.infrastructure.persistence.jpa.mapper;

import org.mapstruct.Mapper;

import com.doankietdev.identityservice.domain.model.dto.OtpCreate;
import com.doankietdev.identityservice.domain.model.entity.Otp;
import com.doankietdev.identityservice.infrastructure.persistence.jpa.entity.OtpEntity;

@Mapper(componentModel = "spring")
public interface OtpMapper extends BaseMapper<Otp, OtpEntity, OtpCreate> {}
