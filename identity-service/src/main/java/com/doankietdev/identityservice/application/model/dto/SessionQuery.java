package com.doankietdev.identityservice.application.model.dto;

import java.time.Instant;

import com.doankietdev.identityservice.shared.model.Paging;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class SessionQuery extends Paging {
  String ipAddress;
  Instant fromLoginDate;
  Instant toLoginDate;
}
