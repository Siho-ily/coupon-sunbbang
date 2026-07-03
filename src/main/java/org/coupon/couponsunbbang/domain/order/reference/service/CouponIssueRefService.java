package org.coupon.couponsunbbang.domain.order.reference.service;

import lombok.RequiredArgsConstructor;
import org.coupon.couponsunbbang.domain.order.reference.entity.CouponIssueRef;
import org.coupon.couponsunbbang.domain.order.reference.entity.CouponIssueRefStatus;
import org.coupon.couponsunbbang.domain.order.reference.repository.CouponIssueRefRepository;
import org.coupon.couponsunbbang.global.exception.BusinessException;
import org.coupon.couponsunbbang.global.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CouponIssueRefService {
	private final CouponIssueRefRepository couponIssueRefRepository;

	public CouponIssueRef getUsableCouponIssue(Long userId, Long couponIssueId) {
		CouponIssueRef couponIssueRef = getCouponIssue(userId, couponIssueId);

		if (couponIssueRef.getStatus() != CouponIssueRefStatus.UNUSED) {
			throw new BusinessException(ErrorCode.VALIDATION_ERROR, "이미 사용했거나 사용할 수 없는 쿠폰입니다.");
		}

		return couponIssueRef;
	}

	@Transactional
	public void useCouponIssue(Long userId, Long couponIssueId) {
		CouponIssueRef couponIssueRef = getUsableCouponIssue(userId, couponIssueId);
		couponIssueRef.use();
	}

	@Transactional
	public void restoreCouponIssue(Long userId, Long couponIssueId) {
		CouponIssueRef couponIssueRef = getCouponIssue(userId, couponIssueId);
		couponIssueRef.restore();
	}

	// ===== * utils * =====
	private CouponIssueRef getCouponIssue(Long userId, Long couponIssueId) {
		return couponIssueRefRepository.findByIdAndUserId(couponIssueId, userId)
				       .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "사용 가능한 쿠폰을 찾을 수 없습니다."));
	}
}
