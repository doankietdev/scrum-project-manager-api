package com.doankietdev.identityservice.infrastructure.persistence.jpa.mapper;

import org.mapstruct.Mapper;

import com.doankietdev.identityservice.domain.model.dto.LoginSessionCreate;
import com.doankietdev.identityservice.domain.model.entity.LoginSession;
import com.doankietdev.identityservice.infrastructure.persistence.jpa.entity.LoginSessionEntity;

@Mapper(componentModel = "spring")
public interface LoginSessionMapper extends BaseMapper<LoginSession, LoginSessionEntity, LoginSessionCreate> {}
