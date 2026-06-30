package org.coupon.couponsunbbang.domain.order.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderDetailResponse(
		Long orderId,
		Long userId,
		Long productId,
		Long couponIssueId,
		Integer quantity,
		BigDecimal originalPrice,
		BigDecimal discountAmount,
		BigDecimal finalPrice,
		LocalDateTime orderedAt
) {
}
