package com.doankietdev.identityservice.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.doankietdev.identityservice.infrastructure.persistence.jpa.repository")
public class JpaConfig {}
