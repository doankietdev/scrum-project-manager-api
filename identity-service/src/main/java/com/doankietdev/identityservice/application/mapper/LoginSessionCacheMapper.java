package com.doankietdev.identityservice.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.doankietdev.identityservice.application.model.cache.LoginSessionCache;
import com.doankietdev.identityservice.domain.model.entity.LoginSession;

@Mapper(componentModel = "spring")
public interface LoginSessionCacheMapper {
  @Mapping(target = "userId", source = "user.id")
  LoginSessionCache toLoginSessionCache(LoginSession loginSession);
}
