package com.doankietdev.profileservice.infrastructure.helper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.doankietdev.profileservice.shared.constants.SortConstants;
import com.doankietdev.profileservice.shared.model.Paging;

public class PageHelper {
  // public static Pageable buildJpaPageable(Paging paging) {
  //   // Sort sort = Sort.by(paging)
  //   Sort.Direction sortDirection = paging.getOrder().equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
  //   Sort sortBy = Sort.by(sortDirection, paging.getSort());
  //   return PageRequest.of(paging.getPage(), paging.getLimit(), sortBy);
  // }
  public static Pageable buildJpaPageable(Paging paging) {
    Integer offset = paging.getPage() - 1;
    Integer limit = paging.getLimit();
    List<Sort.Order> orders = new ArrayList<>();

    if (paging.getSort() != null && !paging.getSort().isBlank()) {
      String[] sortFields = paging.getSort().split(SortConstants.SORT_FIELD_DELIMITER);
      String[] sortOrders = paging.getOrder() != null ? paging.getOrder().split(",") : new String[0];

      for (int i = 0; i < sortFields.length; i++) {
        String field = sortFields[i].trim();
        String order = (i < sortOrders.length) ? sortOrders[i].trim().toUpperCase() : "ASC";

        Sort.Direction direction = Sort.Direction.fromOptionalString(order).orElse(Sort.Direction.ASC);
        orders.add(new Sort.Order(direction, field));
      }

      return PageRequest.of(offset, limit, Sort.by(orders));
    }

    return PageRequest.of(offset, limit);
  }
}
