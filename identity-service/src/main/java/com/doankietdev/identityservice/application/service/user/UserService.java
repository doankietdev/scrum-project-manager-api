package com.doankietdev.identityservice.application.service.user;

import com.doankietdev.identityservice.application.model.dto.GetMySessionsResult;

public interface UserService {
  public GetMySessionsResult getMySessions(String userId);
}
