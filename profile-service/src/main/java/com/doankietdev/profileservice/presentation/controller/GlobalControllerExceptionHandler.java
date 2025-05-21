package com.doankietdev.profileservice.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.doankietdev.profileservice.application.exception.AppException;
import com.doankietdev.profileservice.application.model.enums.AppCode;
import com.doankietdev.profileservice.infrastructure.config.AppProperties;
import com.doankietdev.profileservice.presentation.model.dto.response.ErrorResponse;

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
  ResponseEntity<ErrorResponse<?>> handleException(RuntimeException exception) {
    boolean isDev = appProperties.getEnvName().equals("dev");
    if (isDev) {
      exception.printStackTrace();
    }

    AppCode appCode = AppCode.SERVER_ERROR;

    ErrorResponse<?> response = ErrorResponse.builder()
        .code(appCode.getCode())
        .message(appCode.getMessage())
        .logMessage(isDev ? exception.getMessage() : null)
        .build();
    return ResponseEntity.status(appCode.getStatusCode())
        .body(response);
  }

  @ExceptionHandler(AuthorizationDeniedException.class)
  ResponseEntity<ErrorResponse<?>> handleAuthorizationDeniedException(AuthorizationDeniedException exception) {
    boolean isDev = appProperties.getEnvName().equals("dev");
    if (isDev) {
      exception.printStackTrace();
    }

    AppCode appCode = AppCode.ACCESS_DENIED;

    ErrorResponse<?> response = ErrorResponse.builder()
        .code(appCode.getCode())
        .message(appCode.getMessage())
        .logMessage(isDev ? exception.getMessage() : null)
        .build();
    return ResponseEntity.status(appCode.getStatusCode())
        .body(response);
  }

  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<?> handleNotFound(NoResourceFoundException exception) {
    boolean isDev = appProperties.getEnvName().equals("dev");
    if (isDev) {
      exception.printStackTrace();
    }

    AppCode appCode = AppCode.ENDPOINT_NOT_FOUND;

    ErrorResponse<?> response = ErrorResponse.builder()
        .code(appCode.getCode())
        .message(appCode.getMessage())
        .logMessage(isDev ? exception.getMessage() : null)
        .build();
    return ResponseEntity.status(appCode.getStatusCode())
        .body(response);
  }

  @ExceptionHandler(AppException.class)
  ResponseEntity<ErrorResponse<?>> handleAppException(AppException exception) {
    boolean isDev = appProperties.getEnvName().equals("dev");
    if (isDev) {
      exception.printStackTrace();
    }

    AppCode appCode = exception.getAppCode();

    ErrorResponse<?> response = ErrorResponse.builder()
        .code(appCode.getCode())
        .message(appCode.getMessage())
        .logMessage(isDev ? exception.getLogMessage() : null)
        .build();
    return ResponseEntity.status(appCode.getStatusCode())
        .body(response);
  }
}
