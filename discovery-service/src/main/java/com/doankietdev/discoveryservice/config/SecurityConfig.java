package com.doankietdev.discoveryservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.security.config.Customizer;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
  @Autowired
  CorsConfigurationSource corsConfigurationSource;

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity.authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
        .csrf(AbstractHttpConfigurer::disable)
        .httpBasic(Customizer.withDefaults())
        .cors(cors -> cors.configurationSource(corsConfigurationSource))
        .headers(c -> c.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));
    return httpSecurity.build();
  }
}
