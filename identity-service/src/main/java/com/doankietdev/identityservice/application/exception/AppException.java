package com.doankietdev.identityservice.application.exception;

import com.doankietdev.identityservice.application.model.enums.AppCode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class AppException extends RuntimeException {
  static long serialVersionUID = 1L;
  AppCode appCode;
  String logMessage;
  
  // public AppException(String message) {
  //   super(message);
  // }

  // public AppException(String message, Throwable cause) {
  //   super(message, cause);
  // }

  // public AppException(Throwable cause) {
  //   super(cause);
  // }

  // public AppException(String message, Throwable cause, boolean enableSuppression,
  //     boolean writableStackTrace) {
  //   super(message, cause, enableSuppression, writableStackTrace);
  // }
}
