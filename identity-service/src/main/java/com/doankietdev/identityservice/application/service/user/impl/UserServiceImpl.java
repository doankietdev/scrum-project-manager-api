package com.doankietdev.identityservice.application.service.user.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.doankietdev.identityservice.application.mapper.LoginSessionMapper;
import com.doankietdev.identityservice.application.model.dto.GetMySessionsResult;
import com.doankietdev.identityservice.application.service.user.UserService;
import com.doankietdev.identityservice.domain.model.entity.LoginSession;
import com.doankietdev.identityservice.domain.repository.LoginSessionRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
@Slf4j
public class UserServiceImpl implements UserService {
  LoginSessionRepository loginSessionRepository;
  LoginSessionMapper loginSessionMapper;

  @Override
  public GetMySessionsResult getMySessions(String userId) {
    List<LoginSession> loginSessions = loginSessionRepository.findByUserId(userId);
    return GetMySessionsResult.builder().sessions(loginSessionMapper.toSessionDTOList(loginSessions)).build();
  }
}
