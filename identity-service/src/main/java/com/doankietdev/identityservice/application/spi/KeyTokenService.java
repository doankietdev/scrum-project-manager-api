package com.doankietdev.identityservice.application.spi;

import com.doankietdev.identityservice.application.exception.AppException;
import com.doankietdev.identityservice.application.model.dto.AuthUser;
import com.doankietdev.identityservice.application.model.dto.KeyToken;
import com.doankietdev.identityservice.application.model.dto.TokenPayload;

public interface KeyTokenService {
  KeyToken createKeyToken(AuthUser authUser) throws AppException;
  TokenPayload verifyToken(String token, byte[] publicKey) throws AppException;
  TokenPayload parseToken(String token) throws AppException;
}
