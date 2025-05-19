package com.doankietdev.identityservice.infrastructure.mapper.jpa;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.doankietdev.identityservice.domain.model.dto.LoginSessionCreate;
import com.doankietdev.identityservice.domain.model.dto.SessionSearchCriteria;
import com.doankietdev.identityservice.domain.model.entity.LoginSession;
import com.doankietdev.identityservice.infrastructure.model.criteria.LoginSessionCriteria;
import com.doankietdev.identityservice.infrastructure.persistence.jpa.entity.LoginSessionEntity;

@Mapper(componentModel = "spring")
public interface LoginSessionJpaMapper extends BaseJpaMapper<LoginSession, LoginSessionEntity, LoginSessionCreate> {
  List<LoginSession> toDomainList(List<LoginSessionEntity> entityList);

  @Mapping(target = "userId", ignore = true)
  LoginSessionCriteria toLoginSessionCriteria(SessionSearchCriteria sessionSearchCriteria);
}
