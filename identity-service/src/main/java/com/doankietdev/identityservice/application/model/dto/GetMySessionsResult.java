package com.doankietdev.identityservice.application.model.dto;

import java.util.List;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GetMySessionsResult {
  List<SessionDTO> sessions;
}
