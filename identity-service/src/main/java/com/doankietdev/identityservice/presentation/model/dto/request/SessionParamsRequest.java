package com.doankietdev.identityservice.presentation.model.dto.request;

import java.time.Instant;

import com.doankietdev.identityservice.shared.model.Paging;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SessionParamsRequest extends Paging {
  String ipAddress;
  Instant fromLoginDate;
  Instant toLoginDate;
}
