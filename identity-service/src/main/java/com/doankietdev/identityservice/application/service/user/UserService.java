package com.doankietdev.identityservice.application.service.user;

import com.doankietdev.identityservice.application.model.dto.SessionDTO;
import com.doankietdev.identityservice.application.model.dto.SessionQuery;
import com.doankietdev.identityservice.shared.model.Paginated;

public interface UserService {
  public Paginated<SessionDTO> getUserSessions(String userId, SessionQuery sessionQuery);
}
