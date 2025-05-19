package com.doankietdev.identityservice.presentation.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.doankietdev.identityservice.application.model.dto.SessionDTO;
import com.doankietdev.identityservice.application.model.dto.SessionQuery;
import com.doankietdev.identityservice.presentation.model.dto.request.SessionParamsRequest;
import com.doankietdev.identityservice.presentation.model.dto.response.SessionResponse;
import com.doankietdev.identityservice.shared.model.Paginated;

@Mapper(componentModel = "spring")
public interface UserControllerMapper {
  SessionResponse toSessionResponse(SessionDTO sessionDTO);

  default Paginated<SessionResponse> toSessionResponsePaginated(Paginated<SessionDTO> sessionDTOPaginated) {
     List<SessionResponse> items = sessionDTOPaginated.getItems()
      .stream()
      .map(this::toSessionResponse)
      .toList();

    return Paginated.<SessionResponse>builder()
      .items(items)
      .paging(sessionDTOPaginated.getPaging())
      .totalItems(sessionDTOPaginated.getTotalItems())
      .totalPages(sessionDTOPaginated.getTotalPages())
      .build();
  }
  SessionQuery toSessionQuery(SessionParamsRequest sessionParamsRequest);
}
