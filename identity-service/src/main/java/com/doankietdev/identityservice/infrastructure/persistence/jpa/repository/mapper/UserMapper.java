package com.doankietdev.identityservice.infrastructure.persistence.jpa.repository.mapper;

import org.mapstruct.Mapper;

import com.doankietdev.identityservice.domain.model.dto.UserCreate;
import com.doankietdev.identityservice.domain.model.entity.User;
import com.doankietdev.identityservice.infrastructure.persistence.jpa.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper extends BaseMapper<User, UserEntity, UserCreate> {}
