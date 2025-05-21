package com.doankietdev.profileservice.infrastructure.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.doankietdev.profileservice.application.exception.AppException;
import com.doankietdev.profileservice.application.model.enums.AppCode;
import com.doankietdev.profileservice.infrastructure.config.AppProperties;
import com.doankietdev.profileservice.shared.utils.ResponseUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
  @Autowired
  AppProperties appProperties;

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException exception) {
    boolean isDev = appProperties.getEnvName().equals("dev");
    if (isDev) {
      exception.printStackTrace();
    }

    AppException appException = AppException.from(AppCode.ACCESS_DENIED).withLog(exception.getMessage());
    
    ResponseUtil.outputError(response, appException, isDev);
  }
}