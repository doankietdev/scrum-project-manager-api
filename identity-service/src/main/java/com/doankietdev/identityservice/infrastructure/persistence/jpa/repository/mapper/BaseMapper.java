package com.doankietdev.identityservice.infrastructure.persistence.jpa.repository.mapper;

import org.mapstruct.Mapping;

public interface BaseMapper<D, E, Create> {
  D toDomain(E entity);

  E toEntity(D domain);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "deletedAt", ignore = true)
  E createToEntity(Create data);
}
