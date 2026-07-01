package org.coupon.couponsunbbang.domain.order.dto.response;

import java.math.BigDecimal;

public record OrderPreviewResponse(
		Long productId,
		Long couponIssueId,
		Integer quantity,
		BigDecimal originalPrice,
		BigDecimal discountAmount,
		BigDecimal finalPrice
) {
}
