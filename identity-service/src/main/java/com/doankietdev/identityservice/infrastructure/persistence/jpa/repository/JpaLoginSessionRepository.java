package com.doankietdev.identityservice.infrastructure.persistence.jpa.repository;

import java.util.Objects;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import com.doankietdev.identityservice.domain.model.dto.LoginSessionCreate;
import com.doankietdev.identityservice.domain.model.dto.LoginSessionUpdate;
import com.doankietdev.identityservice.domain.model.dto.SessionSearchCriteria;
import com.doankietdev.identityservice.domain.model.entity.LoginSession;
import com.doankietdev.identityservice.domain.repository.LoginSessionRepository;
import com.doankietdev.identityservice.infrastructure.helper.PageHelper;
import com.doankietdev.identityservice.infrastructure.mapper.jpa.LoginSessionJpaMapper;
import com.doankietdev.identityservice.infrastructure.model.criteria.LoginSessionCriteria;
import com.doankietdev.identityservice.infrastructure.persistence.jpa.entity.LoginSessionEntity;
import com.doankietdev.identityservice.infrastructure.persistence.jpa.specification.SpecificationBuilder;
import com.doankietdev.identityservice.shared.model.Paginated;
import com.doankietdev.identityservice.shared.model.Paging;

import jakarta.annotation.Resource;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Repository
@Slf4j
public class JpaLoginSessionRepository implements LoginSessionRepository {
  LoginSessionJpaStore loginSessionJpaStore;
  LoginSessionJpaMapper loginSessionJpaMapper;

  @Resource
  SpecificationBuilder loginSessionSpecificationBuilder;

  @Override
  public LoginSession findById(String id) {
    return loginSessionJpaMapper.toDomain(loginSessionJpaStore.findById(id).orElse(null));
  }

  @Override
  public LoginSession save(LoginSessionCreate data) {
    LoginSessionEntity loginSessionEntity = loginSessionJpaStore.save(loginSessionJpaMapper.createToEntity(data));
    return loginSessionJpaMapper.toDomain(loginSessionJpaStore.findById(loginSessionEntity.getId())
        .orElse(null));
  }

  @Override
  public LoginSession updateById(String id, LoginSessionUpdate updateData) {
    LoginSessionEntity existsLoginSessionEntity = loginSessionJpaStore.findById(id)
        .orElse(null);
    if (Objects.isNull(existsLoginSessionEntity)) {
      return null;
    }

    BeanUtils.copyProperties(updateData, existsLoginSessionEntity);

    return loginSessionJpaMapper.toDomain(loginSessionJpaStore.save(existsLoginSessionEntity));
  }

  @Override
  public boolean deleteById(String id) {
    try {
      loginSessionJpaStore.deleteById(id);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public boolean deletePermanentById(String id) {
    try {
      loginSessionJpaStore.deletePermanentById(id);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public boolean restoreById(String id) {
    try {
      loginSessionJpaStore.restoreById(id);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public LoginSession findByUserIdAndJti(String userId, String jti) {
    return loginSessionJpaMapper.toDomain(loginSessionJpaStore.findByUserIdAndJti(userId, jti));
  }

  @Override
  public Paginated<LoginSession> findByUserId(String userId, SessionSearchCriteria sessionSearchCriteria) {
    return findByUserIdWithSpec(userId, sessionSearchCriteria);
  }

  private Paginated<LoginSession> findByUserIdWithSpec(String userId, SessionSearchCriteria sessionSearchCriteria) {
    Pageable pageable = PageHelper.buildJpaPageable(sessionSearchCriteria);

    LoginSessionCriteria loginSessionCriteria = loginSessionJpaMapper.toLoginSessionCriteria(sessionSearchCriteria);
    loginSessionCriteria.setUserId(userId);

    Specification<LoginSessionEntity> spec = loginSessionSpecificationBuilder.build(loginSessionCriteria);

    Paginated<LoginSession> paginated = loginSessionJpaMapper.toDomainPaginated(loginSessionJpaStore.findAll(spec, pageable));

    paginated.setPaging(Paging.builder()
    .page(sessionSearchCriteria.getPage())
    .limit(sessionSearchCriteria.getLimit())
    .sort(sessionSearchCriteria.getSort())
    .order(sessionSearchCriteria.getOrder())
    .build());

    return paginated;
  }

  @Override
  public boolean deletePermanentByUserIdAndJti(String userId, String jti) {
    try {
      loginSessionJpaStore.deletePermanentByUserIdAndJti(userId, jti);
      return true;
    } catch (Exception e) {
      log.info("e:: {}", e.getMessage());
      return false;
    }
  }
}
