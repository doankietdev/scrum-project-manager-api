package com.doankietdev.profileservice.infrastructure.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
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

import com.doankietdev.profileservice.infrastructure.config.AppProperties;
import com.doankietdev.profileservice.infrastructure.model.Endpoint;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class SecurityConfig {
  @NonFinal
  Endpoint[] IGNORE_ENDPOINTS = {
      Endpoint.builder().method(HttpMethod.GET).url("/actuator/**").build(),
      Endpoint.builder().method(HttpMethod.POST).url("/actuator/**").build(),
      Endpoint.builder().method(HttpMethod.PATCH).url("/actuator/**").build(),
      Endpoint.builder().method(HttpMethod.PUT).url("/actuator/**").build(),
      Endpoint.builder().method(HttpMethod.POST).url("/rpc/introspect").build()
  };

  CorsConfigurationSource corsConfigurationSource;
  AuthenticationEntryPoint authenticationEntryPoint;
  AccessDeniedHandler accessDeniedHandler;
  AppProperties appProperties;

  @Bean
  SecurityFilterChain securityFilterChain(
      HttpSecurity httpSecurity,
      AuthenticationConfiguration authenticationConfiguration) throws Exception {
    httpSecurity.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
        .csrf(AbstractHttpConfigurer::disable)
        .cors(cors -> cors.configurationSource(corsConfigurationSource))
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .exceptionHandling(
            exception -> exception.authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler))
        .formLogin(login -> login
            .disable())
        .httpBasic(basic -> basic
            .disable())
        .headers(c -> c.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
        .addFilterBefore(
            new GatewayAuthenticationFilter(appProperties, IGNORE_ENDPOINTS),
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
