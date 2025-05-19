package com.doankietdev.identityservice.shared.model;

import java.io.Serializable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED)
public class Paging implements Serializable {
  static final long serialVersionUID = 1L;

  @Builder.Default
  Integer page = 1;

  @Builder.Default
  Integer limit = 10;

  @Builder.Default
  String sort = "createdAt";

  @Builder.Default

  String order = "asc";
}
