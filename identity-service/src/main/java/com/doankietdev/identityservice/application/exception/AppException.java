package com.doankietdev.identityservice.application.exception;

import com.doankietdev.identityservice.application.model.enums.AppCode;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppException extends RuntimeException {
  static long serialVersionUID = 1L;
  AppCode appCode;
  String logMessage;
}
