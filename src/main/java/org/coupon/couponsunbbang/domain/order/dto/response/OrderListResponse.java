package org.coupon.couponsunbbang.domain.order.dto.response;

import java.util.List;

public record OrderListResponse(
		List<OrderListItemResponse> orders,
		int page,
		int size
) {
}
