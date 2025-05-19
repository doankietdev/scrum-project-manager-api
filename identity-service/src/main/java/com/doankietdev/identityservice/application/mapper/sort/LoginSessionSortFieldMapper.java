package com.doankietdev.identityservice.application.mapper.sort;

import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class LoginSessionSortFieldMapper extends AbstractSortFieldMapper {

  private static final Map<String, String> FIELD_MAP = Map.of(
      "loginAt", "createdAt");

  @Override
  protected Map<String, String> getFieldMap() {
    return FIELD_MAP;
  }
}
