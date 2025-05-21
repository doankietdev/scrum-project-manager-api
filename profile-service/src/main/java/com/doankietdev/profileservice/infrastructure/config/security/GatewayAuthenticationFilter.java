package com.doankietdev.profileservice.infrastructure.config.security;

import java.io.IOException;
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

import com.doankietdev.profileservice.application.exception.AppException;
import com.doankietdev.profileservice.application.model.enums.AppCode;
import com.doankietdev.profileservice.infrastructure.config.AppProperties;
import com.doankietdev.profileservice.infrastructure.model.AuthDetails;
import com.doankietdev.profileservice.infrastructure.model.Endpoint;
import com.doankietdev.profileservice.shared.constants.AppHttpHeaders;
import com.doankietdev.profileservice.shared.utils.HttpRequestUtil;
import com.doankietdev.profileservice.shared.utils.ResponseUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GatewayAuthenticationFilter extends OncePerRequestFilter {
  RequestMatcher skipMatcher;
  AppProperties appProperties;

  public GatewayAuthenticationFilter(AppProperties appProperties, Endpoint[] ignoreEndpoints) {
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
    String userId = request.getHeader(AppHttpHeaders.USER_ID);
    String permissionsHeader = request.getHeader(AppHttpHeaders.PERMISSIONS);

    if (StringUtils.isEmpty(userId) || Objects.isNull(permissionsHeader)) {
      throw AppException.from(AppCode.PARAMS_ERROR);
    }

    List<String> permissions = Arrays.stream(permissionsHeader.split(","))
        .map(String::trim)
        .toList();

    try {
      UsernamePasswordAuthenticationToken authentication = getAuthentication(request, response, userId, permissions);
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

      if (isDev) {
        exception.printStackTrace();
      }
      return;
    }

    filterChain.doFilter(request, response);
  }

  private UsernamePasswordAuthenticationToken getAuthentication(
      HttpServletRequest request,
      HttpServletResponse response,
      String userId,
      List<String> permissions) throws AppException {

    List<GrantedAuthority> authorities = permissions.stream()
        .map(permission -> (GrantedAuthority) new SimpleGrantedAuthority(permission))
        .toList();

    AuthDetails details = AuthDetails.builder().clientIp(HttpRequestUtil.getClientIp(request))
        .userAgent(HttpRequestUtil.getUserAgent(request)).build();
    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
        userId,
        null,
        authorities);
    authentication.setDetails(details);
    return authentication;
  }
}
