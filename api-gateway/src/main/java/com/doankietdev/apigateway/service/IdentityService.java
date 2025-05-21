package com.doankietdev.apigateway.service;

import org.springframework.stereotype.Service;

import com.doankietdev.apigateway.client.IdentityClient;
import com.doankietdev.apigateway.dto.response.AppResponse;
import com.doankietdev.apigateway.dto.response.IntrospectResponse;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IdentityService {
  IdentityClient identityClient;

  public Mono<AppResponse<IntrospectResponse>> introspect(String bearerToken) {
    return identityClient.introspect(bearerToken);
  }
}
