package com.doankietdev.identityservice.domain.model.dto;

import com.doankietdev.identityservice.domain.model.enums.OtpStatus;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OtpUpdate {
  OtpStatus status;
}
