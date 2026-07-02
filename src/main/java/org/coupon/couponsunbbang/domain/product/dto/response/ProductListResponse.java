package org.coupon.couponsunbbang.domain.product.dto.response;

import java.util.List;

public record ProductListResponse(
		List<ProductResponse> products,
		int page,
		int size,
		long totalElements,
		int totalPages,
		boolean hasNext
) {
}
