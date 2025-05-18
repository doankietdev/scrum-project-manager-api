package com.doankietdev.identityservice.application.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.doankietdev.identityservice.application.model.cache.LoginSessionCache;
import com.doankietdev.identityservice.application.model.dto.SessionDTO;
import com.doankietdev.identityservice.domain.model.entity.LoginSession;

@Mapper(componentModel = "spring")
public interface LoginSessionMapper {
  @Mapping(target = "userId", source = "user.id")
  LoginSessionCache toLoginSessionCache(LoginSession loginSession);

  @Mapping(target = "loginAt", source = "createdAt")
  SessionDTO toSessionDTO(LoginSession loginSession);

  List<SessionDTO> toSessionDTOList(List<LoginSession> loginSessions);
}
