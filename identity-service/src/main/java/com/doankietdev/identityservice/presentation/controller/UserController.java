package com.doankietdev.identityservice.presentation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.doankietdev.identityservice.application.model.dto.SessionDTO;
import com.doankietdev.identityservice.application.model.dto.SessionQuery;
import com.doankietdev.identityservice.application.service.user.UserService;
import com.doankietdev.identityservice.infrastructure.model.auth.AuthUser;
import com.doankietdev.identityservice.presentation.mapper.UserControllerMapper;
import com.doankietdev.identityservice.presentation.model.dto.request.SessionParamsRequest;
import com.doankietdev.identityservice.presentation.model.dto.response.AppResponse;
import com.doankietdev.identityservice.presentation.model.dto.response.SessionResponse;
import com.doankietdev.identityservice.shared.model.Paginated;

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
  public ResponseEntity<AppResponse> getMySessions(SessionParamsRequest params, Authentication authentication) {
    AuthUser authUser = (AuthUser) authentication.getPrincipal();

    SessionQuery sessionQuery = userControllerMapper.toSessionQuery(params);

    Paginated<SessionDTO> sessionDTOPaginated = userService.getMySessions(authUser.getId(), sessionQuery);

    Paginated<SessionResponse> sessionResponsePaginated = userControllerMapper.toSessionResponsePaginated(sessionDTOPaginated);

    AppResponse<Paginated<SessionResponse>> response = AppResponse.<Paginated<SessionResponse>>builder()
        .data(sessionResponsePaginated)
        .build();

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
