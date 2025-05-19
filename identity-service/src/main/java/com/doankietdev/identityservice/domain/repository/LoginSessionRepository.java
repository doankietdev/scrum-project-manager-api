package com.doankietdev.identityservice.domain.repository;

import org.springframework.stereotype.Repository;

import com.doankietdev.identityservice.domain.model.dto.LoginSessionCreate;
import com.doankietdev.identityservice.domain.model.dto.LoginSessionUpdate;
import com.doankietdev.identityservice.domain.model.dto.SessionSearchCriteria;
import com.doankietdev.identityservice.domain.model.entity.LoginSession;
import com.doankietdev.identityservice.shared.model.Paginated;

@Repository
public interface LoginSessionRepository extends BaseRepository<LoginSession, LoginSessionCreate, LoginSessionUpdate> {
  LoginSession findByUserIdAndJti(String userId, String jti);

  Paginated<LoginSession> findByUserId(String userId, SessionSearchCriteria searchCriteria);

  boolean deletePermanentByUserIdAndJti(String userId, String jti);
}
