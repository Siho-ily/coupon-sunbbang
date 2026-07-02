package org.coupon.couponsunbbang.domain.order.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderPreviewRequest(
		@NotNull(message = "상품 ID는 필수입니다.")
		Long productId,

		Long couponIssueId,

		@NotNull(message = "주문 수량은 필수입니다.")
		@Positive(message = "주문 수량은 1개 이상이어야 합니다.")
		Integer quantity
) {
}
