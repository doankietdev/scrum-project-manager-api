package com.doankietdev.identityservice.application.model.dto;

import java.time.Instant;

import com.doankietdev.identityservice.shared.model.Paging;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SessionQuery extends Paging {
  String ipAddress;
  Instant fromLoginDate;
  Instant toLoginDate;
}
