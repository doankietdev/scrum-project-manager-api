package com.doankietdev.apigateway.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class IntrospectResponse {
  List<String> authorities;
  String userId;
}
