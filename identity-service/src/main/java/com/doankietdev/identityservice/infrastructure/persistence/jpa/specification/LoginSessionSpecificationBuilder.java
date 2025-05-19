package com.doankietdev.identityservice.infrastructure.persistence.jpa.specification;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.doankietdev.identityservice.infrastructure.model.criteria.LoginSessionCriteria;
import com.doankietdev.identityservice.infrastructure.persistence.jpa.entity.LoginSessionEntity;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LoginSessionSpecificationBuilder
    implements SpecificationBuilder<LoginSessionEntity, LoginSessionCriteria> {

  @Override
  public Specification<LoginSessionEntity> build(LoginSessionCriteria loginSessionCriteria) {
    return (Root<LoginSessionEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
      List<Predicate> predicates = new ArrayList<>();

      String userId = loginSessionCriteria.getUserId();
      String ipAddress = loginSessionCriteria.getIpAddress();
      Instant fromLoginDate = loginSessionCriteria.getFromLoginDate();
      Instant toLoginDate = loginSessionCriteria.getToLoginDate();

      if (userId != null && !userId.isEmpty()) {
        predicates.add(cb.equal(root.get("user").get("id"), userId));
      }

      if (ipAddress != null && !ipAddress.isEmpty()) {
        predicates.add(cb.equal(root.get("ipAddress"), ipAddress));
      }

      if (fromLoginDate != null & toLoginDate != null) {
        predicates.add(cb.between(root.get("createdAt"), fromLoginDate, toLoginDate));
      } else if (fromLoginDate != null) {
        predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), fromLoginDate));
      } else if (toLoginDate != null) {
        predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), toLoginDate));
      }

      // if (params.getProductId() != null && !params.getProductId().isEmpty()) {
      //   predicates.add(cb.equal(root.get("productId"), params.getProductId()));
      // }

      // if (params.getProductName() != null && !params.getProductName().isEmpty()) {
      //   predicates.add(cb.like(root.get("productName"), "%" + params.getProductName() + "%"));
      // }

      // if (params.getIds() != null && !params.getIds().isEmpty()) {
      //   predicates.add(root.get("id").in(params.getIds()));
      // }

      // if (params.getStoreId() != null && !params.getStoreId().isEmpty()) {
      //   predicates.add(cb.equal(root.get("storeId"), params.getStoreId()));
      // }

      // if (params.getStoreName() != null && !params.getStoreName().isEmpty()) {
      //   predicates.add(cb.like(root.get("storeName"), "%" + params.getStoreName() + "%"));
      // }

      // if (params.getCategoryPath() != null && !params.getCategoryPath().isEmpty()) {
      //   predicates.add(cb.like(root.get("categoryPath"), "%" + params.getCategoryPath() + "%"));
      // }

      // if (params.getIsSelfOperated() != null) {
      //   predicates.add(cb.equal(root.get("isSelfOperated"), params.getIsSelfOperated()));
      // }

      // if (params.getDisplayStatus() != null && !params.getDisplayStatus().isEmpty()) {
      //   predicates.add(cb.equal(root.get("displayStatus"), params.getDisplayStatus()));
      // }

      // if (params.getIsApproved() != null && !params.getIsApproved().isEmpty()) {
      //   predicates.add(cb.equal(root.get("isApproved"), params.getIsApproved()));
      // }

      // if (params.getMaxQuantity() != null) {
      //   predicates.add(cb.le(root.get("quantity"), params.getMaxQuantity()));
      // }

      // if (params.getMinQuantity() != null) {
      //   predicates.add(cb.ge(root.get("quantity"), params.getMinQuantity()));
      // }

      // if (params.getRecommend() != null) {
      //   predicates.add(cb.equal(root.get("recommend"), params.getRecommend()));
      // }

      // if (params.getProductType() != null && !params.getProductType().isEmpty()) {
      //   predicates.add(cb.equal(root.get("productType"), params.getProductType()));
      // }

      // if (params.getSalesModel() != null && !params.getSalesModel().isEmpty()) {
      //   predicates.add(cb.equal(root.get("salesModel"), params.getSalesModel()));
      // }

      // if (params.getAlertQuantity() != null && params.getAlertQuantity()) {
      //   predicates.add(cb.lessThanOrEqualTo(root.get("quantity"), root.get("alertQuantity")));
      // }

      // if (params.getMinPrice() != null && params.getMaxPrice() != null) {
      //   predicates.add(cb.between(root.get("price"), params.getMinPrice(), params.getMaxPrice()));
      // } else if (params.getMinPrice() != null) {
      //   predicates.add(cb.ge(root.get("price"), params.getMinPrice()));
      // }

      return cb.and(predicates.toArray(new Predicate[0]));
    };
  }
}
