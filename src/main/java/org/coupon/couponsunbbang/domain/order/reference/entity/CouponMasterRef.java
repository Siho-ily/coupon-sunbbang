package org.coupon.couponsunbbang.domain.order.reference.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * 쿠폰 도메인 구현 전, 주문 할인 계산에 필요한 쿠폰 정책만 참조하는 모델입니다.
 */
@Entity
@Table(name = "coupon_master")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponMasterRef {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "event_id", nullable = false)
	private Long eventId;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "discount_type", nullable = false)
	private String discountType;

	@Column(name = "discount_value", nullable = false, precision = 10, scale = 2)
	private BigDecimal discountValue;

	@Column(name = "total_quantity", nullable = false)
	private Integer totalQuantity;

	@Column(name = "issue_expired_at", nullable = false)
	private LocalDateTime issueExpiredAt;

	@Column(name = "use_expired_at", nullable = false)
	private LocalDateTime useExpiredAt;

	@CreationTimestamp
	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
}
