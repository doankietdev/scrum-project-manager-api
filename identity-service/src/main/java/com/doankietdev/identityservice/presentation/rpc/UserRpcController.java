package com.doankietdev.identityservice.presentation.rpc;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.doankietdev.identityservice.application.model.dto.SessionDTO;
import com.doankietdev.identityservice.application.model.dto.SessionQuery;
import com.doankietdev.identityservice.application.service.user.UserService;
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
@RequestMapping("/rpc/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserRpcController {
  UserService userService;
  UserControllerMapper userControllerMapper;

  @GetMapping("/{userId}/sessions")
  public ResponseEntity<AppResponse> getUserSessions(@PathVariable String userId, SessionParamsRequest params) {
    SessionQuery sessionQuery = userControllerMapper.toSessionQuery(params);

    Paginated<SessionDTO> sessionDTOPaginated = userService.getUserSessions(userId, sessionQuery);

    Paginated<SessionResponse> sessionResponsePaginated = userControllerMapper.toSessionResponsePaginated(sessionDTOPaginated);

    AppResponse<Paginated<SessionResponse>> response = AppResponse.<Paginated<SessionResponse>>builder()
        .data(sessionResponsePaginated)
        .build();

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
