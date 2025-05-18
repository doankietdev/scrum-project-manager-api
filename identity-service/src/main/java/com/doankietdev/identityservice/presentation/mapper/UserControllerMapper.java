package com.doankietdev.identityservice.presentation.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.doankietdev.identityservice.application.model.dto.SessionDTO;
import com.doankietdev.identityservice.presentation.model.dto.response.GetMySessionsResponse;

@Mapper(componentModel = "spring")
public interface UserControllerMapper {
  GetMySessionsResponse toGetMySessionsResponse (List<SessionDTO> session);
}
