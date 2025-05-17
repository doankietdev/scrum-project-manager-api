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
import com.doankietdev.identityservice.application.model.dto.TokenPayload;
import com.doankietdev.identityservice.application.model.enums.AppCode;
import com.doankietdev.identityservice.application.spi.KeyTokenService;
import com.doankietdev.identityservice.domain.model.entity.LoginSession;
import com.doankietdev.identityservice.domain.model.entity.User;
import com.doankietdev.identityservice.domain.repository.LoginSessionRepository;
import com.doankietdev.identityservice.domain.repository.UserRepository;
import com.doankietdev.identityservice.infrastructure.model.Endpoint;
import com.doankietdev.identityservice.infrastructure.model.enums.RequestHeaderEnum;
import com.doankietdev.identityservice.infrastructure.utils.ResponseUtil;

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
  LoginSessionRepository loginSessionRepository;
  UserRepository userRepository;
  RequestMatcher skipMatcher;

  public JwtAuthenticationFilter(KeyTokenService keyTokenService,
      LoginSessionRepository loginSessionRepository, UserRepository userRepository, Endpoint[] publicEndpoints) {
    this.keyTokenService = keyTokenService;
    this.loginSessionRepository = loginSessionRepository;
    this.userRepository = userRepository;

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
      UsernamePasswordAuthenticationToken authentication = getAuthentication(authToken, response);
      if (Objects.isNull(authentication)) {
        return;
      }
      SecurityContextHolder.getContext().setAuthentication(authentication);
    } catch (Exception e) {
      if (e instanceof AppException) {
        AppException appException = (AppException) e;
        ResponseUtil.output(response, appException.getAppCode());
      }
      ResponseUtil.output(response, AppCode.SERVER_ERROR);
      return;
    }
    
    filterChain.doFilter(request, response);
  }

  private UsernamePasswordAuthenticationToken getAuthentication(
      String authToken,
      HttpServletResponse response) throws AppException {
    if (StringUtils.isEmpty(authToken)) {
      throw AppException.builder().appCode(AppCode.TOKEN_MISSING).build();
    }

    TokenPayload parsedTokenPayload = keyTokenService.parseToken(authToken);
    if (Objects.isNull(parsedTokenPayload)) {
      throw AppException.builder().appCode(AppCode.SERVER_ERROR).build();
    }

    User user = userRepository.findById(parsedTokenPayload.getUserId());
    if (Objects.isNull(user)) {
      throw AppException.builder().appCode(AppCode.SERVER_ERROR).build();
    }

    LoginSession loginSession = loginSessionRepository.findByUserIdAndJti(
        parsedTokenPayload.getUserId(),
        parsedTokenPayload.getJti());
    if (Objects.isNull(loginSession)) {
      throw AppException.builder().appCode(AppCode.SERVER_ERROR).build();
    }

    TokenPayload tokenPayload = keyTokenService.verifyToken(authToken, loginSession.getPublicKey());
    if (Objects.isNull(tokenPayload)) {
      throw AppException.builder().appCode(AppCode.SERVER_ERROR).build();
    }

    List<GrantedAuthority> authorities = new ArrayList<>();
    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
        user.getIdentifier(),
        null,
        authorities);
    authentication.setDetails(user);
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
