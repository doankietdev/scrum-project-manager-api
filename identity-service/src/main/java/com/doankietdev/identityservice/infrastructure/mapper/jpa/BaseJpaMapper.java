package com.doankietdev.identityservice.infrastructure.mapper.jpa;

import java.util.stream.Collectors;

import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import com.doankietdev.identityservice.shared.model.Paginated;

public interface BaseJpaMapper<D, E, Create> {
  D toDomain(E entity);

  E toEntity(D domain);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "deletedAt", ignore = true)
  E createToEntity(Create data);

  default Paginated<D> toDomainPaginated(Page<E> entityPage) {
     return Paginated.<D>builder()
        .items(entityPage.getContent().stream().map(this::toDomain).collect(Collectors.toList()))
        .totalItems(entityPage.getTotalElements())
        .totalPages(entityPage.getTotalPages())
        .build();
  }
}
