package com.doankietdev.identityservice.infrastructure.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TokenPayload {
    String userId;
    String jti;
}
