package com.doankietdev.identityservice.shared.model;

import java.io.Serializable;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Paging implements Serializable {
  static final long serialVersionUID = 1L;

  Integer page = 1;
  Integer limit = 10;
  String sort = "createdAt";
  String order = "asc";
}
