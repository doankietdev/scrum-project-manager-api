package com.doankietdev.identityservice.application.service.auth.cache;

import com.doankietdev.identityservice.application.model.cache.LoginSessionCache;

public interface LoginSessionCacheService {
  public boolean put(String userId, String jti, LoginSessionCache loginSessionCache);
  public LoginSessionCache get(String userId, String jti);
}
