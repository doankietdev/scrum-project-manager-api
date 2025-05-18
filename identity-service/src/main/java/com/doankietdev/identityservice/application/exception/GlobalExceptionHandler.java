package com.doankietdev.identityservice.application.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.doankietdev.identityservice.application.model.dto.response.ErrorResponse;
import com.doankietdev.identityservice.application.model.enums.AppCode;
import com.doankietdev.identityservice.infrastructure.config.AppProperties;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class GlobalExceptionHandler {
  AppProperties appProperties;

  @ExceptionHandler(value = RuntimeException.class)
  ResponseEntity<ErrorResponse<?>> handleException(RuntimeException e) {
    boolean isDev = appProperties.getEnvName().equals("dev");
  
    if (isDev) {
      log.error("Stack trace:", e);
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

  @ExceptionHandler(value = AppException.class)
  ResponseEntity<ErrorResponse<?>> handleAppException(AppException e) {
    boolean isDev = appProperties.getEnvName().equals("dev");

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
