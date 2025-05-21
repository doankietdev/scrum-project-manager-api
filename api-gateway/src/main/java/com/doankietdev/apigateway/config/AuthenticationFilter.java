package com.doankietdev.apigateway.config;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;

import com.doankietdev.apigateway.dto.response.ErrorResponse;
import com.doankietdev.apigateway.enums.AppCode;
import com.doankietdev.apigateway.service.IdentityService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
public class AuthenticationFilter implements GlobalFilter, Ordered {
  IdentityService identityService;
  ObjectMapper objectMapper;

  @NonFinal
  private String[] publicEndpoints = {
      "/auth/register",
      "/auth/verify",
      "/auth/login",
      "/auth/logout",
  };

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    if (isPublicEndpoint(exchange.getRequest()))
      return chain.filter(exchange);

    List<String> authHeader =  exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);
    if (Objects.isNull(authHeader) || CollectionUtils.isEmpty(authHeader))
            return unauthenticated(exchange.getResponse());

    String bearerToken = authHeader.getFirst();
    if (bearerToken.isEmpty())
      return unauthenticated(exchange.getResponse());

    return identityService.introspect(bearerToken).flatMap(introspectResponse -> {
      if (introspectResponse.getCode() == AppCode.SUCCESS.getCode())
        return chain.filter(exchange);
      else
        return unauthenticated(exchange.getResponse());
    }).onErrorResume(throwable -> unauthenticated(exchange.getResponse()));
  }

  @Override
  public int getOrder() {
    return -1;
  }

  private boolean isPublicEndpoint(ServerHttpRequest request) {
    return Arrays.stream(publicEndpoints)
        .anyMatch(s -> request.getURI().getPath().matches(s));
  }

  Mono<Void> unauthenticated(ServerHttpResponse response) {
    ErrorResponse<?> errorResponse = ErrorResponse.builder()
        .code(1401)
        .message("Unauthenticated")
        .build();

    String body = null;
    try {
      body = objectMapper.writeValueAsString(errorResponse);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }

    response.setStatusCode(HttpStatus.UNAUTHORIZED);
    response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

    return response.writeWith(
        Mono.just(response.bufferFactory().wrap(body.getBytes())));
  }
}
