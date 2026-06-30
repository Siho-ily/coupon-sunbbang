package org.coupon.couponsunbbang.domain.order.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderListItemResponse(
		Long orderId,
		Long productId,
		Integer quantity,
		BigDecimal finalPrice,
		LocalDateTime orderedAt
) {
}
