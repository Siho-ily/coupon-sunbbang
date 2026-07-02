package org.coupon.couponsunbbang.domain.order.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "`order`")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Column(name = "product_id", nullable = false)
	private Long productId;

	@Column(name = "coupon_issue_id")
	private Long couponIssueId;

	@Column(nullable = false)
	private Integer quantity;

	@Column(name = "original_price", nullable = false, precision = 10, scale = 2)
	private BigDecimal originalPrice;

	@Column(name = "discount_price", nullable = false, precision = 10, scale = 2)
	private BigDecimal discountPrice;

	@Column(name = "final_price", nullable = false, precision = 10, scale = 2)
	private BigDecimal finalPrice;

	@CreationTimestamp
	@Column(name = "created_at")
	private LocalDateTime createdAt;

	/**
	 * 주문 엔티티를 생성하는 메서드입니다.
	 *
	 * @param userId 유저 ID
	 * @param productId 상품 ID
	 * @param couponIssueId 쿠폰이슈 ID
	 * @param quantity 수량
	 * @param originalPrice 초기 금액
	 * @param discountPrice 할인 금액
	 * @param finalPrice 최종 금액
	 */
	public static Order create(
			Long userId,
			Long productId,
			Long couponIssueId,
			Integer quantity,
			BigDecimal originalPrice,
			BigDecimal discountPrice,
			BigDecimal finalPrice
	) {
		Order order = new Order();
		order.userId = userId;
		order.productId = productId;
		order.couponIssueId = couponIssueId;
		order.quantity = quantity;
		order.originalPrice = originalPrice;
		order.discountPrice = discountPrice;
		order.finalPrice = finalPrice;
		return order;
	}
}

