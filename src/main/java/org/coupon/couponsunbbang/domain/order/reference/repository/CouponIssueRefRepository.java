package org.coupon.couponsunbbang.domain.order.reference.repository;

import org.coupon.couponsunbbang.domain.order.reference.entity.CouponIssueRef;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CouponIssueRefRepository extends JpaRepository<CouponIssueRef, Long> {
	// 쿠폰 발급 조회 + 소유자 검증
	Optional<CouponIssueRef> findByIdAndUserId(Long couponIssueId, Long userId);
}
