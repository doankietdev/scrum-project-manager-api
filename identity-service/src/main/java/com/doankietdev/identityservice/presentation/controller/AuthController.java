package com.doankietdev.identityservice.presentation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.doankietdev.identityservice.application.model.dto.LoginResult;
import com.doankietdev.identityservice.application.model.dto.RegisterResult;
import com.doankietdev.identityservice.application.model.dto.VerifyAccountResult;
import com.doankietdev.identityservice.application.service.auth.AuthService;
import com.doankietdev.identityservice.presentation.mapper.AuthControllerMapper;
import com.doankietdev.identityservice.presentation.model.dto.request.LoginRequest;
import com.doankietdev.identityservice.presentation.model.dto.request.RegisterRequest;
import com.doankietdev.identityservice.presentation.model.dto.request.VerifyAccountRequest;
import com.doankietdev.identityservice.presentation.model.dto.response.AppResponse;
import com.doankietdev.identityservice.presentation.model.dto.response.LoginResponse;
import com.doankietdev.identityservice.presentation.model.dto.response.RegisterResponse;
import com.doankietdev.identityservice.presentation.model.dto.response.VerifyAccountResponse;
import com.doankietdev.identityservice.shared.utils.HttpRequestUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthController {
  AuthService authService;
  AuthControllerMapper authControllerMapper;

  @PostMapping("/register")
  public ResponseEntity<AppResponse> register(@RequestBody RegisterRequest request) {

    RegisterResult result = authService.register(authControllerMapper.toRegisterCommand(request));

    AppResponse<RegisterResponse> response = AppResponse.<RegisterResponse>builder()
        .data(authControllerMapper.toRegisterResponse(result))
        .build();

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PostMapping("/verify")
  public ResponseEntity<AppResponse> verifyAccount(@RequestBody VerifyAccountRequest request) {
    VerifyAccountResult result = authService.verifyAccount(authControllerMapper.toVerifyAccountCommand(request));

    AppResponse<VerifyAccountResponse> response = AppResponse.<VerifyAccountResponse>builder()
        .data(authControllerMapper.toVerifyAccountResponse(result))
        .build();
        
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @PostMapping("/login")
  public ResponseEntity<AppResponse> login(@RequestBody LoginRequest request,
      HttpServletRequest httpServletRequest) {

    String clientIp = HttpRequestUtil.getClientIp(httpServletRequest);
    String userAgent = HttpRequestUtil.getUserAgent(httpServletRequest);

    LoginResult result = authService.login(authControllerMapper.toLoginCommand(request), clientIp, userAgent);

    AppResponse<LoginResponse> response = AppResponse.<LoginResponse>builder()
        .data(authControllerMapper.toLoginResponse(result))
        .build();

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
