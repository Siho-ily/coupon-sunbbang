package org.coupon.couponsunbbang.domain.order.reference.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.coupon.couponsunbbang.domain.order.reference.entity.CouponMasterRef;
import org.coupon.couponsunbbang.domain.order.reference.repository.CouponMasterRefRepository;
import org.coupon.couponsunbbang.global.exception.BusinessException;
import org.coupon.couponsunbbang.global.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CouponMasterRefService {
	private static final String FIXED = "FIXED";
	private static final String RATE = "RATE";
	private static final BigDecimal PERCENT_BASE = new BigDecimal("100.00");

	private final CouponMasterRefRepository couponMasterRefRepository;

	public CouponMasterRef getUsableCouponMaster(Long couponMasterId) {
		CouponMasterRef couponMasterRef = couponMasterRefRepository.findById(couponMasterId)
				.orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "쿠폰 정책을 찾을 수 없습니다."));

		if (couponMasterRef.getUseExpiredAt().isBefore(LocalDateTime.now())) {
			throw new BusinessException(ErrorCode.VALIDATION_ERROR, "사용 기한이 지난 쿠폰입니다.");
		}

		return couponMasterRef;
	}

	public BigDecimal calculateDiscountPrice(BigDecimal originalPrice, CouponMasterRef couponMasterRef) {
		BigDecimal discountPrice = switch (couponMasterRef.getDiscountType()) {
			case FIXED -> couponMasterRef.getDiscountValue();
			case RATE -> originalPrice.multiply(couponMasterRef.getDiscountValue())
					.divide(PERCENT_BASE, 2, RoundingMode.HALF_UP);
			default -> throw new BusinessException(ErrorCode.VALIDATION_ERROR, "지원하지 않는 쿠폰 할인 타입입니다.");
		};

		if (discountPrice.compareTo(originalPrice) > 0) {
			return originalPrice;
		}

		return discountPrice;
	}
}
