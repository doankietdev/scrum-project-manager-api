package com.doankietdev.identityservice.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.doankietdev.identityservice.application.exception.AppException;
import com.doankietdev.identityservice.application.model.enums.AppCode;
import com.doankietdev.identityservice.infrastructure.config.AppProperties;
import com.doankietdev.identityservice.presentation.model.dto.response.ErrorResponse;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class GlobalControllerExceptionHandler {
  AppProperties appProperties;

  @ExceptionHandler(RuntimeException.class)
  ResponseEntity<ErrorResponse<?>> handleException(RuntimeException e) {
    boolean isDev = appProperties.getEnvName().equals("dev");

    if (isDev) {
      e.printStackTrace();
    }

    AppCode appCode = AppCode.SERVER_ERROR;

    ErrorResponse<?> response = ErrorResponse.builder()
        .code(appCode.getCode())
        .message(appCode.getMessage())
        .logMessage(isDev ? e.getMessage() : null)
        .build();
    return ResponseEntity.status(appCode.getStatusCode())
        .body(response);
  }

  @ExceptionHandler(AuthorizationDeniedException.class)
  ResponseEntity<ErrorResponse<?>> handleAuthorizationDeniedException(AuthorizationDeniedException e) {
    boolean isDev = appProperties.getEnvName().equals("dev");

    if (isDev) {
      e.printStackTrace();
    }

    AppCode appCode = AppCode.ACCESS_DENIED;

    ErrorResponse<?> response = ErrorResponse.builder()
        .code(appCode.getCode())
        .message(appCode.getMessage())
        .logMessage(isDev ? e.getMessage() : null)
        .build();
    return ResponseEntity.status(appCode.getStatusCode())
        .body(response);
  }

  @ExceptionHandler(AppException.class)
  ResponseEntity<ErrorResponse<?>> handleAppException(AppException e) {
    boolean isDev = appProperties.getEnvName().equals("dev");

    if (isDev) {
      e.printStackTrace();
    }

    AppCode appCode = e.getAppCode();

    ErrorResponse<?> response = ErrorResponse.builder()
        .code(appCode.getCode())
        .message(appCode.getMessage())
        .logMessage(isDev ? e.getLogMessage() : null)
        .build();
    return ResponseEntity.status(appCode.getStatusCode())
        .body(response);
  }
}
