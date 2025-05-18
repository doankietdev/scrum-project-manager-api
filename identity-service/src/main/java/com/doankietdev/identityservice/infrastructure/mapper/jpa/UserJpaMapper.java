package com.doankietdev.identityservice.infrastructure.mapper.jpa;

import org.mapstruct.Mapper;

import com.doankietdev.identityservice.domain.model.dto.UserCreate;
import com.doankietdev.identityservice.domain.model.entity.User;
import com.doankietdev.identityservice.infrastructure.persistence.jpa.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface UserJpaMapper extends BaseJpaMapper<User, UserEntity, UserCreate> {}
