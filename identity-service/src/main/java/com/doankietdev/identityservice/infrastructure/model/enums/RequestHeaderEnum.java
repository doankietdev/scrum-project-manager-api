package com.doankietdev.identityservice.infrastructure.model.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum RequestHeaderEnum {
    AUTHORIZATION_HEADER("Authorization");

    String value;
}
