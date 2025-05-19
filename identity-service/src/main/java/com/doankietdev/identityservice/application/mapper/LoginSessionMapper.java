package com.doankietdev.identityservice.application.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.doankietdev.identityservice.application.model.cache.LoginSessionCached;
import com.doankietdev.identityservice.application.model.dto.SessionDTO;
import com.doankietdev.identityservice.application.model.dto.SessionQuery;
import com.doankietdev.identityservice.domain.model.dto.SessionSearchCriteria;
import com.doankietdev.identityservice.domain.model.entity.LoginSession;
import com.doankietdev.identityservice.shared.model.Paginated;

@Mapper(componentModel = "spring")
public interface LoginSessionMapper {
  @Mapping(target = "userId", source = "user.id")
  LoginSessionCached toLoginSessionCached(LoginSession loginSession);

  @Mapping(target = "loginAt", source = "createdAt")
  SessionDTO toSessionDTO(LoginSession loginSession);

  default Paginated<SessionDTO> toSessionDTOPaginated(Paginated<LoginSession> loginSessionPaginated) {
    List<SessionDTO> items = loginSessionPaginated.getItems()
      .stream()
      .map(this::toSessionDTO)
      .toList();

    return Paginated.<SessionDTO>builder()
      .items(items)
      .paging(loginSessionPaginated.getPaging())
      .totalItems(loginSessionPaginated.getTotalItems())
      .totalPages(loginSessionPaginated.getTotalPages())
      .build();
  }

  SessionSearchCriteria toSessionSearchCriteria(SessionQuery sessionQuery);
}
