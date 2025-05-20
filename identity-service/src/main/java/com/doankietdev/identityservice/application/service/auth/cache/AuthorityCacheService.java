package com.doankietdev.identityservice.application.service.auth.cache;

import com.doankietdev.identityservice.application.model.cache.AuthoritiesCache;

public interface AuthorityCacheService {
  public boolean put(String userId, AuthoritiesCache authoritiesCache);
  AuthoritiesCache get(String userId);
  public boolean delete(String userId);
}
