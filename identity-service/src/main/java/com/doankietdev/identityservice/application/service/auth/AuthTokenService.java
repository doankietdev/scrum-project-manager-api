package com.doankietdev.identityservice.application.service.auth;

import com.doankietdev.identityservice.application.model.dto.AuthKeyToken;
import com.doankietdev.identityservice.application.model.dto.AuthUser;

public interface AuthTokenService {
  AuthKeyToken createAuthKeyToken(AuthUser authUser);
}
