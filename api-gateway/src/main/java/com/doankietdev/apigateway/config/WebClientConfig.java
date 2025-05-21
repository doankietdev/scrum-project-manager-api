package com.doankietdev.apigateway.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import com.doankietdev.apigateway.client.IdentityClient;

@Configuration
public class WebClientConfig {
  @Autowired
  AppProperties appProperties;

  @Bean
  @LoadBalanced
  WebClient.Builder loadBalancedWebClientBuilder() {
    return WebClient.builder();
  }

  @Bean
  CorsWebFilter corsWebFilter() {
    CorsConfiguration corsConfiguration = new CorsConfiguration();
    corsConfiguration.setAllowedOrigins(List.of("*"));
    corsConfiguration.setAllowedHeaders(List.of("*"));
    corsConfiguration.setAllowedMethods(List.of("*"));

    UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
    urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);

    return new CorsWebFilter(urlBasedCorsConfigurationSource);
  }

  @Bean
  IdentityClient identityClient(WebClient.Builder builder) {
    WebClient webClient = builder.baseUrl(appProperties.getServices().getIdentityService().getBaseUrl()).build();

    HttpServiceProxyFactory factory = HttpServiceProxyFactory
        .builderFor(WebClientAdapter.create(webClient))
        .build();

    return factory.createClient(IdentityClient.class);
  }
}
