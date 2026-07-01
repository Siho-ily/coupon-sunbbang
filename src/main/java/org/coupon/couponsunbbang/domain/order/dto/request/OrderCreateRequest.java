package org.coupon.couponsunbbang.domain.order.dto.request;

public record OrderCreateRequest(
		Long productId,
		Long couponIssueId,
		Integer quantity
) {
}