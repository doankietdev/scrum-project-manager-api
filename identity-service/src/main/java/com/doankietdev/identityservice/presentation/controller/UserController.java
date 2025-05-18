package com.doankietdev.identityservice.presentation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.doankietdev.identityservice.application.model.AuthUser;
import com.doankietdev.identityservice.application.model.dto.GetMySessionsResult;
import com.doankietdev.identityservice.application.model.dto.response.AppResponse;
import com.doankietdev.identityservice.application.service.user.UserService;
import com.doankietdev.identityservice.presentation.mapper.UserControllerMapper;
import com.doankietdev.identityservice.presentation.model.dto.response.GetMySessionsResponse;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {
  UserService userService;
  UserControllerMapper userControllerMapper;

  @GetMapping("/me/sessions")
  public ResponseEntity<AppResponse> getMySessions(Authentication authentication) {
    AuthUser authUser = (AuthUser) authentication.getPrincipal();

    GetMySessionsResult getMySessionsResult = userService.getMySessions(authUser.getId());

    GetMySessionsResponse getMySessionsResponse = userControllerMapper.toGetMySessionsResponse(getMySessionsResult.getSessions());

    AppResponse<GetMySessionsResponse> response = AppResponse.<GetMySessionsResponse>builder()
        .data(getMySessionsResponse)
        .build();

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
