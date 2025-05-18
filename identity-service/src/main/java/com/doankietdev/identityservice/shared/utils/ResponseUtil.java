package com.doankietdev.identityservice.shared.utils;

import java.io.IOException;

import org.springframework.http.MediaType;

import com.doankietdev.identityservice.application.model.enums.AppCode;
import com.doankietdev.identityservice.presentation.model.dto.response.AppResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ResponseUtil {
  static String ENCODING = "UTF-8";
  static String CONTENT_TYPE = MediaType.APPLICATION_JSON_VALUE;

  public static void output(HttpServletResponse response, AppCode appCode) {
    AppResponse<?> appResponse = AppResponse.builder()
          .code(appCode.getCode())
          .message(appCode.getMessage())
          .build();

    ServletOutputStream servletOutputStream = null;
    try {
      response.setCharacterEncoding(ENCODING);
      response.setContentType(CONTENT_TYPE);
      response.setStatus(appCode.getStatusCode().value());

      ObjectMapper objectMapper = new ObjectMapper();

      servletOutputStream = response.getOutputStream();
      servletOutputStream.write(objectMapper.writeValueAsBytes(appResponse));
    } catch (Exception e) {
      log.error("Output response error:", e);
    } finally {
      if (servletOutputStream != null) {
        try {
          servletOutputStream.flush();
          servletOutputStream.close();
        } catch (IOException e) {
          log.error("Closing output response error:", e);
        }
      }
    }
  }
}
