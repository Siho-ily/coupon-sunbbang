package org.coupon.couponsunbbang.domain.product.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.coupon.couponsunbbang.domain.product.entity.Product;

public record ProductResponse(
		Long productId,
		String title,
		BigDecimal price,
		LocalDateTime createdAt
) {
	public static ProductResponse from(Product product) {
		return new ProductResponse(
				product.getId(),
				product.getTitle(),
				product.getPrice(),
				product.getCreatedAt()
		);
	}
}
