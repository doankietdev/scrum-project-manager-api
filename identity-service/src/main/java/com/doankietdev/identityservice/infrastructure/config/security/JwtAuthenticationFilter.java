package com.doankietdev.identityservice.infrastructure.config.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.doankietdev.identityservice.application.exception.AppException;
import com.doankietdev.identityservice.application.model.cache.AuthoritiesCache;
import com.doankietdev.identityservice.application.model.cache.LoginSessionCache;
import com.doankietdev.identityservice.application.model.dto.TokenPayload;
import com.doankietdev.identityservice.application.model.enums.AppCode;
import com.doankietdev.identityservice.application.service.auth.cache.AuthorityCacheService;
import com.doankietdev.identityservice.application.service.auth.cache.LoginSessionCacheService;
import com.doankietdev.identityservice.application.spi.KeyTokenService;
import com.doankietdev.identityservice.infrastructure.config.AppProperties;
import com.doankietdev.identityservice.infrastructure.model.Endpoint;
import com.doankietdev.identityservice.infrastructure.model.auth.AuthDetails;
import com.doankietdev.identityservice.infrastructure.model.auth.AuthUser;
import com.doankietdev.identityservice.shared.utils.HttpRequestUtil;
import com.doankietdev.identityservice.shared.utils.ResponseUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  KeyTokenService keyTokenService;
  LoginSessionCacheService loginSessionCacheService;
  AuthorityCacheService authorityCacheService;
  RequestMatcher skipMatcher;
  AppProperties appProperties;

  public JwtAuthenticationFilter(KeyTokenService keyTokenService,
      LoginSessionCacheService loginSessionCacheService, AuthorityCacheService authorityCacheService,
      AppProperties appProperties,
      Endpoint[] ignoreEndpoints) {
    this.keyTokenService = keyTokenService;
    this.loginSessionCacheService = loginSessionCacheService;
    this.authorityCacheService = authorityCacheService;
    this.appProperties = appProperties;

    List<RequestMatcher> matchers = Arrays.stream(ignoreEndpoints)
        .map(ep -> new AntPathRequestMatcher(ep.getUrl(), ep.getMethod().name()))
        .collect(Collectors.toList());
    this.skipMatcher = new OrRequestMatcher(matchers);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    return skipMatcher.matches(request);
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain) throws IOException, ServletException {
    String authHeader = request.getHeader("Authorization");
    String authToken = extractTokenFromAuthHeader(authHeader);

    try {
      UsernamePasswordAuthenticationToken authentication = getAuthentication(request, response, authToken);
      if (Objects.isNull(authentication)) {
        return;
      }
      SecurityContextHolder.getContext().setAuthentication(authentication);
    } catch (Exception exception) {
      Boolean isDev = appProperties.getEnvName().equals("dev");
      if (exception instanceof AppException) {
        AppException appException = (AppException) exception;
        ResponseUtil.outputError(response, appException, isDev);
      } else {
        AppException appException = AppException.from(AppCode.SERVER_ERROR).withLog(exception.getMessage());
        ResponseUtil.outputError(response, appException, isDev);
      }
      return;
    }

    filterChain.doFilter(request, response);
  }

  private UsernamePasswordAuthenticationToken getAuthentication(
      HttpServletRequest request,
      HttpServletResponse response,
      String authToken) throws AppException {
    if (StringUtils.isEmpty(authToken)) {
      throw AppException.from(AppCode.TOKEN_MISSING);
    }

    TokenPayload parsedTokenPayload = keyTokenService.parseToken(authToken);
    if (Objects.isNull(parsedTokenPayload)) {
      throw AppException.from(AppCode.SERVER_ERROR);
    }

    LoginSessionCache loginSessionCache = loginSessionCacheService.get(parsedTokenPayload.getUserId(),
        parsedTokenPayload.getJti());

    if (Objects.isNull(loginSessionCache.getLoginSession())) {
      throw AppException.from(AppCode.TOKEN_INVALID).withLog(authToken);
    }

    TokenPayload tokenPayload = keyTokenService.verifyToken(authToken,
        loginSessionCache.getLoginSession().getPublicKey());
    if (Objects.isNull(tokenPayload)) {
      throw AppException.from(AppCode.SERVER_ERROR).withLog("Failed to verify token");
    }

    AuthoritiesCache authoritiesCache = authorityCacheService.get(tokenPayload.getUserId());

    List<GrantedAuthority> authorities = new ArrayList<>();
    if (Objects.nonNull(authoritiesCache.getAuthorities())) {
      authorities = authoritiesCache.getAuthorities().stream()
          .map(name -> (GrantedAuthority) new SimpleGrantedAuthority(name))
          .toList();
    }

    AuthUser principle = AuthUser.builder().id(tokenPayload.getUserId()).jti(tokenPayload.getJti()).build();
    AuthDetails details = AuthDetails.builder().clientIp(HttpRequestUtil.getClientIp(request))
        .userAgent(HttpRequestUtil.getUserAgent(request)).build();
    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
        principle,
        null,
        authorities);
    authentication.setDetails(details);
    return authentication;
  }

  private String extractTokenFromAuthHeader(String authHeader) {
    String prefix = "Bearer ";
    if (StringUtils.hasText(authHeader) && authHeader.startsWith(prefix)) {
      return authHeader.substring(prefix.length()).trim();
    }
    return null;
  }
}
