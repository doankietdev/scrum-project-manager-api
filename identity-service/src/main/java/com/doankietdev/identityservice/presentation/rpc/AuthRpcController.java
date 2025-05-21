package com.doankietdev.identityservice.presentation.rpc;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.doankietdev.identityservice.infrastructure.model.auth.AuthUser;
import com.doankietdev.identityservice.presentation.model.dto.response.AppResponse;
import com.doankietdev.identityservice.presentation.model.dto.response.IntrospectResponse;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/rpc/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthRpcController {
  @PostMapping("/introspect")
  public ResponseEntity<AppResponse> introspect(Authentication authentication) {
    AuthUser authUser = (AuthUser) authentication.getPrincipal();

    IntrospectResponse introspectResponse = IntrospectResponse.builder()
      .userId(authUser.getId())
      .authorities(authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
      .build();

    AppResponse<IntrospectResponse> response = AppResponse.<IntrospectResponse>builder()
      .data(introspectResponse)
      .build();

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
