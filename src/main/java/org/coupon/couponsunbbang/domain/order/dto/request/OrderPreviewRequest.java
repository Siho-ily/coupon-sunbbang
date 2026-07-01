package org.coupon.couponsunbbang.domain.order.dto.request;

public record OrderPreviewRequest(
		Long productId,
		Long couponIssueId,
		Integer quantity
) {
}
