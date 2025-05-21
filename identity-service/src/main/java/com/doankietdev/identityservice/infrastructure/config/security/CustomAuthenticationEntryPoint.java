package com.doankietdev.identityservice.infrastructure.config.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.doankietdev.identityservice.application.exception.AppException;
import com.doankietdev.identityservice.application.model.enums.AppCode;
import com.doankietdev.identityservice.infrastructure.config.AppProperties;
import com.doankietdev.identityservice.shared.utils.ResponseUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
  @Autowired
  AppProperties appProperties;

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
      throws IOException {
    boolean isDev = appProperties.getEnvName().equals("dev");
    if (isDev) {
      exception.printStackTrace();
    }

    AppException appException = AppException.from(AppCode.TOKEN_INVALID).withLog(exception.getMessage());

    ResponseUtil.outputError(response, appException, isDev);
  }
}
