package com.doankietdev.apigateway.client;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import com.doankietdev.apigateway.dto.response.AppResponse;
import com.doankietdev.apigateway.dto.response.IntrospectResponse;

import reactor.core.publisher.Mono;

@HttpExchange(contentType = MediaType.APPLICATION_JSON_VALUE)
public interface IdentityClient {
  @PostExchange(url = "/auth/internal/introspect")
  Mono<AppResponse<IntrospectResponse>> introspect(
      @RequestHeader(value = HttpHeaders.AUTHORIZATION) String bearerToken);
}
