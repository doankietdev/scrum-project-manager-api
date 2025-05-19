package com.doankietdev.identityservice.infrastructure.persistence.jpa.specification;

import org.springframework.data.jpa.domain.Specification;

public interface SpecificationBuilder<Entity, Criteria> {
  Specification<Entity> build(Criteria criteria);
}
