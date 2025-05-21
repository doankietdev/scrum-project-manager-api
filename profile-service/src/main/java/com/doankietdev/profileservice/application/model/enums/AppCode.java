package com.doankietdev.profileservice.application.model.enums;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public enum AppCode {
        SUCCESS(HttpStatus.OK.value(), "Success", HttpStatus.OK),
        PARAMS_ERROR(HttpStatus.BAD_REQUEST.value(), "Invalid parameters", HttpStatus.BAD_REQUEST),
        ENDPOINT_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "This endpoint is not supported", HttpStatus.NOT_FOUND),
        SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Server error, please try again later",
                        HttpStatus.INTERNAL_SERVER_ERROR),
        RATE_LIMIT_ERROR(HttpStatus.TOO_MANY_REQUESTS.value(), "Access too frequently, please try again later",
                        HttpStatus.TOO_MANY_REQUESTS),

        TOKEN_MISSING(10205, "Missing token", HttpStatus.UNAUTHORIZED),
        TOKEN_INVALID(10206, "Invalid token", HttpStatus.UNAUTHORIZED),
        ACCESS_DENIED(10209, "Access denied", HttpStatus.FORBIDDEN)

        ;

        int code;
        String message;
        HttpStatusCode statusCode;
}