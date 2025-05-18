package com.doankietdev.identityservice.application.exception;

import com.doankietdev.identityservice.application.model.enums.AppCode;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppException extends RuntimeException {
  static final long serialVersionUID = 1L;
  final AppCode appCode;
  String logMessage;

  public AppException(AppCode appCode) {
    super(appCode.getMessage());
    this.appCode = appCode;

    StackTraceElement[] currentStack = Thread.currentThread().getStackTrace();
    /*
     * Thread.currentThread().getStackTrace() trả về mảng StackTraceElement như sau:
     * [0] = getStackTrace()
     * [1] = constructor này (AppException.<init>)
     * [2] = nơi throw AppException
     * ...
     *
     * Bạn có thể cắt từ vị trí [2] trở đi để stack trace bắt đầu tại nơi throw.
     */
    if (currentStack.length > 3) {
      StackTraceElement[] trimmedStack = new StackTraceElement[currentStack.length - 3];
      System.arraycopy(currentStack, 3, trimmedStack, 0, trimmedStack.length);
      this.setStackTrace(trimmedStack);
    }
  }

  public static AppException from(AppCode appCode) {
    return new AppException(appCode);
  }

  public AppException withLog(String logMessage) {
    this.logMessage = logMessage;
    return this;
  }
}
