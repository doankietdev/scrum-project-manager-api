package com.doankietdev.identityservice.infrastructure.persistence.jpa.mapper;

public interface BaseMapper<D, E, Create> {
  D toDomain(E entity);

  E toEntity(D domain);

  E createToEntity(Create data);
}
