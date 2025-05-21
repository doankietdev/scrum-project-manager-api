package com.doankietdev.profileservice.shared.model;

import java.io.Serializable;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder(access = AccessLevel.PUBLIC)
@AllArgsConstructor()
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Paginated<E> implements Serializable {
  static final long serialVersionUID = 1L;
  List<E> items;
  Paging paging;
  Long totalItems;
  Integer totalPages;
}
