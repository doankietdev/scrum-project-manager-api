package com.doankietdev.identityservice.infrastructure.security;

import com.doankietdev.identityservice.application.model.dto.AuthKeyToken;
import com.doankietdev.identityservice.application.model.dto.AuthUser;

public interface KeyTokenInfrasService {
  AuthKeyToken createKeyToken(AuthUser authUser);
}
