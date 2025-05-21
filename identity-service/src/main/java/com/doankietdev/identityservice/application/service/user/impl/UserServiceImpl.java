package com.doankietdev.identityservice.application.service.user.impl;

import java.util.Objects;

import org.springframework.stereotype.Service;

import com.doankietdev.identityservice.application.exception.AppException;
import com.doankietdev.identityservice.application.mapper.LoginSessionMapper;
import com.doankietdev.identityservice.application.mapper.sort.SortFieldMapper;
import com.doankietdev.identityservice.application.model.dto.SessionDTO;
import com.doankietdev.identityservice.application.model.dto.SessionQuery;
import com.doankietdev.identityservice.application.model.enums.AppCode;
import com.doankietdev.identityservice.application.service.user.UserService;
import com.doankietdev.identityservice.domain.model.dto.SessionSearchCriteria;
import com.doankietdev.identityservice.domain.model.entity.LoginSession;
import com.doankietdev.identityservice.domain.model.entity.User;
import com.doankietdev.identityservice.domain.repository.LoginSessionRepository;
import com.doankietdev.identityservice.domain.repository.UserRepository;
import com.doankietdev.identityservice.shared.model.Paginated;

import jakarta.annotation.Resource;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
@Slf4j
public class UserServiceImpl implements UserService {
  UserRepository userRepository;
  LoginSessionRepository loginSessionRepository;
  LoginSessionMapper loginSessionMapper;

  @Resource
  SortFieldMapper loginSessionSortFieldMapper;

  @Override
  public Paginated<SessionDTO> getUserSessions(String userId, SessionQuery sessionQuery) {
    User user = userRepository.findById(userId);
    if (Objects.isNull(user)) throw AppException.from(AppCode.USER_NOT_FOUND);

    sessionQuery.setSort(loginSessionSortFieldMapper.map(sessionQuery.getSort()));
    SessionSearchCriteria searchCriteria = loginSessionMapper.toSessionSearchCriteria(sessionQuery);

    Paginated<LoginSession> paginated = loginSessionRepository.findByUserId(userId, searchCriteria);
    return loginSessionMapper.toSessionDTOPaginated(paginated);
  }
}
