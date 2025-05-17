package com.doankietdev.identityservice.infrastructure.model;

import org.springframework.http.HttpMethod;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level =  AccessLevel.PRIVATE, makeFinal = true)
public class Endpoint {
    HttpMethod method;
    String url;
}
