package com.doankietdev.identityservice.domain.model.dto;

import java.time.Instant;

import com.doankietdev.identityservice.shared.model.Paging;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

@Data
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SessionSearchCriteria extends Paging {
  String ipAddress;
  Instant fromLoginDate;
  Instant toLoginDate;
}
