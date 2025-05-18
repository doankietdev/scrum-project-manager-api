package com.doankietdev.identityservice.infrastructure.config.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.doankietdev.identityservice.application.exception.AppException;
import com.doankietdev.identityservice.application.model.AuthDetails;
import com.doankietdev.identityservice.application.model.AuthUser;
import com.doankietdev.identityservice.application.model.cache.LoginSessionCache;
import com.doankietdev.identityservice.application.model.dto.TokenPayload;
import com.doankietdev.identityservice.application.model.enums.AppCode;
import com.doankietdev.identityservice.application.service.auth.cache.LoginSessionCacheService;
import com.doankietdev.identityservice.application.spi.KeyTokenService;
import com.doankietdev.identityservice.infrastructure.model.Endpoint;
import com.doankietdev.identityservice.infrastructure.model.enums.RequestHeaderEnum;
import com.doankietdev.identityservice.infrastructure.utils.ResponseUtil;
import com.doankietdev.identityservice.shared.utils.IpUtils;

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
  RequestMatcher skipMatcher;

  public JwtAuthenticationFilter(KeyTokenService keyTokenService,
  LoginSessionCacheService loginSessionCacheService, Endpoint[] publicEndpoints) {
    this.keyTokenService = keyTokenService;
    this.loginSessionCacheService = loginSessionCacheService;

    List<RequestMatcher> matchers = Arrays.stream(publicEndpoints)
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
    String authHeader = request.getHeader(RequestHeaderEnum.AUTHORIZATION_HEADER.getValue());
    String authToken = extractTokenFromAuthHeader(authHeader);

    try {
      UsernamePasswordAuthenticationToken authentication = getAuthentication(request, response, authToken);
      if (Objects.isNull(authentication)) {
        return;
      }
      SecurityContextHolder.getContext().setAuthentication(authentication);
    } catch (Exception e) {
      if (e instanceof AppException) {
        AppException appException = (AppException) e;
        ResponseUtil.output(response, appException.getAppCode());
      } else {
        e.printStackTrace();
        ResponseUtil.output(response, AppCode.SERVER_ERROR);
      }
      return;
    }
    
    filterChain.doFilter(request, response);
  }

  private UsernamePasswordAuthenticationToken getAuthentication(
      HttpServletRequest request,
      HttpServletResponse response,
      String authToken
      ) throws AppException {
    if (StringUtils.isEmpty(authToken)) {
      throw AppException.builder().appCode(AppCode.TOKEN_MISSING).build();
    }

    TokenPayload parsedTokenPayload = keyTokenService.parseToken(authToken);
    if (Objects.isNull(parsedTokenPayload)) {
      throw AppException.builder().appCode(AppCode.SERVER_ERROR).build();
    }

    LoginSessionCache loginSessionCache = loginSessionCacheService.get(parsedTokenPayload.getUserId(),
    parsedTokenPayload.getJti());

    if (Objects.isNull(loginSessionCache)) {
      throw AppException.builder().appCode(AppCode.TOKEN_INVALID).build();
    }

    TokenPayload tokenPayload = keyTokenService.verifyToken(authToken, loginSessionCache.getPublicKey());
    if (Objects.isNull(tokenPayload)) {
      throw AppException.builder().appCode(AppCode.SERVER_ERROR).build();
    }

    AuthUser principle = AuthUser.builder().id(tokenPayload.getUserId()).build();
    AuthDetails details = AuthDetails.builder().clientIp(IpUtils.getClientIp(request)).build();
    List<GrantedAuthority> authorities = new ArrayList<>();
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
