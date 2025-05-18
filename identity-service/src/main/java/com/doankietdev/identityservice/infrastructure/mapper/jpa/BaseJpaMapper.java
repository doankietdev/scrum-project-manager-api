package com.doankietdev.identityservice.infrastructure.mapper.jpa;

import org.mapstruct.Mapping;

public interface BaseJpaMapper<D, E, Create> {
  D toDomain(E entity);

  E toEntity(D domain);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "deletedAt", ignore = true)
  E createToEntity(Create data);
}
