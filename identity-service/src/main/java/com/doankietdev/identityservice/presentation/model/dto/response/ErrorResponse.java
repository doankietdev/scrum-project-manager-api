package com.doankietdev.identityservice.presentation.model.dto.response;

import com.doankietdev.identityservice.application.model.enums.AppCode;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse<T> {
  @Builder.Default
  int code = AppCode.SUCCESS.getCode();

  @Builder.Default
  String message = AppCode.SUCCESS.getMessage();

  String logMessage;

  @Builder.Default
  long timestamp = System.currentTimeMillis();

  T details;
}
