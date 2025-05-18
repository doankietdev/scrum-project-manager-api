package com.doankietdev.identityservice.domain.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.doankietdev.identityservice.domain.model.dto.LoginSessionCreate;
import com.doankietdev.identityservice.domain.model.dto.LoginSessionUpdate;
import com.doankietdev.identityservice.domain.model.entity.LoginSession;

@Repository
public interface LoginSessionRepository extends BaseRepository<LoginSession, LoginSessionCreate, LoginSessionUpdate> {
  LoginSession findByUserIdAndJti(String userId, String jti);

  List<LoginSession> findByUserId(String userId);
}
