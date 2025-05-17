package com.doankietdev.identityservice.infrastructure.security;

import com.doankietdev.identityservice.application.exception.AppException;
import com.doankietdev.identityservice.infrastructure.model.AuthUser;
import com.doankietdev.identityservice.infrastructure.model.KeyToken;
import com.doankietdev.identityservice.infrastructure.model.TokenPayload;

public interface KeyTokenInfrasService {
  KeyToken createKeyToken(AuthUser authUser) throws AppException;
  TokenPayload verifyToken(String token, byte[] publicKey) throws AppException;
  TokenPayload parseToken(String token) throws AppException;
}
