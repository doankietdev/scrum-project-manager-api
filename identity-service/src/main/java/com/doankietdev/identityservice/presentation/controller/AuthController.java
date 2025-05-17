package com.doankietdev.identityservice.presentation.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.doankietdev.identityservice.application.model.dto.request.AccountVerifyRequest;
import com.doankietdev.identityservice.application.model.dto.request.LoginRequest;
import com.doankietdev.identityservice.application.model.dto.request.RegisterRequest;
import com.doankietdev.identityservice.application.model.dto.response.AccountVerifyResponse;
import com.doankietdev.identityservice.application.model.dto.response.AppResponse;
import com.doankietdev.identityservice.application.model.dto.response.LoginResponse;
import com.doankietdev.identityservice.application.model.dto.response.RegisterResponse;
import com.doankietdev.identityservice.application.service.auth.AuthService;

import io.micrometer.common.util.StringUtils;
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

  @PostMapping("/register")
  public ResponseEntity<AppResponse<RegisterResponse>> register(@RequestBody RegisterRequest request) {
    AppResponse<RegisterResponse> response = AppResponse.<RegisterResponse>builder()
        .data(authService.register(request))
        .build();
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PostMapping("/verify")
  public ResponseEntity<AppResponse<AccountVerifyResponse>> verifyAccount(@RequestBody AccountVerifyRequest request) {
    AppResponse<AccountVerifyResponse> response = AppResponse.<AccountVerifyResponse>builder()
        .data(authService.verifyAccount(request))
        .build();
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @PostMapping("/login")
  public ResponseEntity<AppResponse<LoginResponse>> login(@RequestBody LoginRequest request,
      HttpServletRequest httpServletRequest) {

    String clientIp = getClientIp(httpServletRequest);
    String userAgent = httpServletRequest.getHeader("User-Agent");

    AppResponse<LoginResponse> response = AppResponse.<LoginResponse>builder()
        .data(authService.login(request, clientIp, userAgent))
        .build();
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  private String getClientIp(HttpServletRequest request) {
    String LOCALHOST_IPV4 = "127.0.0.1";
	  String LOCALHOST_IPV6 = "0:0:0:0:0:0:0:1";

    String ipAddress = request.getHeader("X-Forwarded-For");
    if (StringUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
      ipAddress = request.getHeader("Proxy-Client-IP");
    }

    if (StringUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
      ipAddress = request.getHeader("WL-Proxy-Client-IP");
    }

    if (StringUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
      ipAddress = request.getRemoteAddr();
      if (LOCALHOST_IPV4.equals(ipAddress) || LOCALHOST_IPV6.equals(ipAddress)) {
        try {
          InetAddress inetAddress = InetAddress.getLocalHost();
          ipAddress = inetAddress.getHostAddress();
        } catch (UnknownHostException e) {
          e.printStackTrace();
        }
      }
    }

    if (!StringUtils.isEmpty(ipAddress)
        && ipAddress.length() > 15
        && ipAddress.indexOf(",") > 0) {
      ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
    }

    return ipAddress;
  }
}
