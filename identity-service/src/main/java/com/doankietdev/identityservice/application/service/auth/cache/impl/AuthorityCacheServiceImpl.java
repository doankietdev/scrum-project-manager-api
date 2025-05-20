package com.doankietdev.identityservice.application.service.auth.cache.impl;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;

import com.doankietdev.identityservice.application.model.cache.AuthoritiesCache;
import com.doankietdev.identityservice.application.service.auth.cache.AuthorityCacheService;
import com.doankietdev.identityservice.application.spi.DistributedCacheService;
import com.doankietdev.identityservice.domain.model.entity.Permission;
import com.doankietdev.identityservice.domain.model.entity.User;
import com.doankietdev.identityservice.domain.model.enums.PermissionType;
import com.doankietdev.identityservice.domain.repository.UserRepository;
import com.doankietdev.identityservice.shared.utils.CachePrefix;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthorityCacheServiceImpl implements AuthorityCacheService {
  DistributedCacheService<AuthoritiesCache> distributedCacheService;
  UserRepository userRepository;

  @Override
  public boolean put(String userId, AuthoritiesCache authoritiesCache) {
    return distributedCacheService.put(getAuthoriesCacheKey(userId), authoritiesCache, 30L);
  }

  @Override
  public AuthoritiesCache get(String userId) {
    AuthoritiesCache authoritiesCache = distributedCacheService.get(getAuthoriesCacheKey(userId));
    if (Objects.isNull(authoritiesCache)) {
      authoritiesCache = getFromDatabase(userId);
    }
    return authoritiesCache;
  }

  private AuthoritiesCache getFromDatabase(String userId) {
    User user = userRepository.findById(userId);
    if (Objects.isNull(user)) {
      AuthoritiesCache authoritiesCache = AuthoritiesCache.builder().authorities(null).build();
      put(userId, authoritiesCache);
      return authoritiesCache;
    }

    List<String> permissions = user.getRoles().stream()
        .flatMap(role -> role.getPermissions().stream())
        .filter(permission -> permission.getType() == PermissionType.API)
        .map(Permission::getName)
        .distinct()
        .toList();

    AuthoritiesCache authoritiesCache = AuthoritiesCache.builder().authorities(permissions).build();
    put(userId, authoritiesCache);
    return authoritiesCache;
  }

  @Override
  public boolean delete(String userId) {
    return distributedCacheService.remove(getAuthoriesCacheKey(userId));
  }

  private String getAuthoriesCacheKey(String userId) {
    return CachePrefix.AUTHORITIES.getPrefix() + userId;
  }
}
