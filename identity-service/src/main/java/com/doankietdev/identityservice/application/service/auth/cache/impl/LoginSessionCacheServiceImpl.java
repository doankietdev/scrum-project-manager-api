package com.doankietdev.identityservice.application.service.auth.cache.impl;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.doankietdev.identityservice.application.mapper.LoginSessionMapper;
import com.doankietdev.identityservice.application.model.cache.LoginSessionCache;
import com.doankietdev.identityservice.application.model.cache.LoginSessionCached;
import com.doankietdev.identityservice.application.service.auth.cache.LoginSessionCacheService;
import com.doankietdev.identityservice.application.spi.DistributedCacheService;
import com.doankietdev.identityservice.domain.model.entity.LoginSession;
import com.doankietdev.identityservice.domain.repository.LoginSessionRepository;
import com.doankietdev.identityservice.infrastructure.config.AppProperties;
import com.doankietdev.identityservice.shared.utils.CachePrefix;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class LoginSessionCacheServiceImpl implements LoginSessionCacheService {
  DistributedCacheService<LoginSessionCache> distributedCacheService;
  LoginSessionRepository loginSessionRepository;
  LoginSessionMapper loginSessionMapper;
  AppProperties appProperties;

  @Override
  public boolean put(String userId, String jti, LoginSessionCache loginSessionCache) {
    return distributedCacheService.put(getLoginSessionCacheKey(userId, jti), loginSessionCache, appProperties.getAuth().getRefreshTokenExpirationTime());
  }

  @Override
  public LoginSessionCache get(String userId, String jti) {
    LoginSessionCache loginSessionCache = distributedCacheService.get(getLoginSessionCacheKey(userId, jti));

    if (Objects.isNull(loginSessionCache)) {
      loginSessionCache = getFromDatabase(userId, jti);
    }
    return loginSessionCache;
  }

  @Override
  public boolean deleteLoginSession(String userId, String jti) {
    return distributedCacheService.remove(getLoginSessionCacheKey(userId, jti));
  }

  private LoginSessionCache getFromDatabase(String userId, String jti) {
    LoginSession loginSession = loginSessionRepository.findByUserIdAndJti(userId, jti);
    LoginSessionCached loginSessionCached = loginSessionMapper.toLoginSessionCached(loginSession);
    LoginSessionCache loginSessionCache = LoginSessionCache.builder().loginSession(loginSessionCached).build();
    put(userId, jti, loginSessionCache);
    return loginSessionCache;
  }
  
  private String getLoginSessionCacheKey(String userId, String jti) {
    return CachePrefix.LOGIN_SESSION.getPrefix() + userId + ":" + jti;
  }
}
