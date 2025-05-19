package com.doankietdev.identityservice.presentation.model.dto.request;

import java.time.Instant;

import com.doankietdev.identityservice.shared.model.Paging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SessionParamsRequest extends Paging {
  String ipAddress;
  Instant fromLoginDate;
  Instant toLoginDate;
}
