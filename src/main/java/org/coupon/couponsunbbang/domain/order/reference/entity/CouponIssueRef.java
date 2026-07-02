package org.coupon.couponsunbbang.domain.order.reference.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

/**
 * 쿠폰 도메인 구현 전, 주문 생성에 필요한 쿠폰 발급 정보만 참조하는 모델입니다.
 */
@Entity
@Table(name = "coupon_issue")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponIssueRef {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Column(name = "coupon_master_id", nullable = false)
	private Long couponMasterId;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private CouponIssueRefStatus status;

	@CreationTimestamp
	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "used_at")
	private LocalDateTime usedAt;

	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;
}
