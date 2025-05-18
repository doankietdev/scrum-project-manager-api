package com.doankietdev.identityservice.infrastructure.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import com.doankietdev.identityservice.application.service.auth.cache.LoginSessionCacheService;
import com.doankietdev.identityservice.application.spi.KeyTokenService;
import com.doankietdev.identityservice.infrastructure.model.Endpoint;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SecurityConfig {
  final Endpoint[] PUBLIC_ENDPOINTS = {
      Endpoint.builder().method(HttpMethod.POST).url("/auth/register").build(),
      Endpoint.builder().method(HttpMethod.POST).url("/auth/login").build(),
      Endpoint.builder().method(HttpMethod.POST).url("/auth/verify").build()
  };

  @Autowired
  CorsConfigurationSource corsConfigurationSource;

  @Autowired
  AuthenticationEntryPoint authenticationEntryPoint;

  @Autowired
  AccessDeniedHandler accessDeniedHandler;

  @Autowired
  KeyTokenService keyTokenService;

  @Autowired
  LoginSessionCacheService loginSessionCacheService;

  @Bean
  SecurityFilterChain securityFilterChain(
      HttpSecurity httpSecurity,
      AuthenticationConfiguration authenticationConfiguration) throws Exception {
    httpSecurity.authorizeHttpRequests(authorize -> {
      for (Endpoint endpoint : PUBLIC_ENDPOINTS) {
        authorize.requestMatchers(endpoint.getMethod(), endpoint.getUrl()).permitAll();
      }
      authorize.anyRequest().authenticated();
    })
        .csrf(AbstractHttpConfigurer::disable)
        .cors(cors -> cors.configurationSource(corsConfigurationSource))
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .exceptionHandling(
            c -> c.authenticationEntryPoint(authenticationEntryPoint).accessDeniedHandler(accessDeniedHandler))
        .headers(c -> c.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));

    httpSecurity.addFilterBefore(new JwtAuthenticationFilter(keyTokenService, loginSessionCacheService, PUBLIC_ENDPOINTS),
        UsernamePasswordAuthenticationFilter.class);

    return httpSecurity.build();
  }

  @Bean
  AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
      return config.getAuthenticationManager();
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(10);
  }
}
