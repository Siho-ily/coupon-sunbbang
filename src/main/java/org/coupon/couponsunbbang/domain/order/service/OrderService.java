package org.coupon.couponsunbbang.domain.order.service;

import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.coupon.couponsunbbang.domain.order.dto.request.OrderCreateRequest;
import org.coupon.couponsunbbang.domain.order.dto.request.OrderPreviewRequest;
import org.coupon.couponsunbbang.domain.order.dto.response.OrderCreateResponse;
import org.coupon.couponsunbbang.domain.order.dto.response.OrderDeleteResponse;
import org.coupon.couponsunbbang.domain.order.dto.response.OrderDetailResponse;
import org.coupon.couponsunbbang.domain.order.dto.response.OrderListResponse;
import org.coupon.couponsunbbang.domain.order.dto.response.OrderPreviewResponse;
import org.coupon.couponsunbbang.domain.order.entity.Order;
import org.coupon.couponsunbbang.domain.order.reference.entity.CouponIssueRef;
import org.coupon.couponsunbbang.domain.order.reference.entity.CouponMasterRef;
import org.coupon.couponsunbbang.domain.order.reference.service.CouponIssueRefService;
import org.coupon.couponsunbbang.domain.order.reference.service.CouponMasterRefService;
import org.coupon.couponsunbbang.domain.order.repository.OrderRepository;
import org.coupon.couponsunbbang.domain.product.entity.Product;
import org.coupon.couponsunbbang.domain.product.exception.ProductNotFoundException;
import org.coupon.couponsunbbang.domain.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {
	private final OrderRepository orderRepository;
	private final ProductRepository productRepository;
	private final CouponIssueRefService couponIssueRefService;
	private final CouponMasterRefService couponMasterRefService;

	public OrderListResponse getOrders(Long userId, int page, int size) {
		throw new UnsupportedOperationException("주문 목록 조회 로직 미구현");
	}

	public OrderDetailResponse getOrderDetail(Long userId, Long orderId) {
		throw new UnsupportedOperationException("주문 상세 조회 로직 미구현");
	}

	@Transactional
	public OrderCreateResponse createOrder(Long userId, OrderCreateRequest request) {
		// 상품 조회
		Product product = productRepository.findById(request.productId())
				.orElseThrow(ProductNotFoundException::new);

		// 금액 계산
		BigDecimal originalPrice = product.getPrice().multiply(BigDecimal.valueOf(request.quantity()));
		BigDecimal discountPrice = calculateDiscountPrice(userId, request.couponIssueId(), originalPrice);
		BigDecimal finalPrice = originalPrice.subtract(discountPrice);

		// 주문 생성
		Order order = Order.create(
				userId,
				request.productId(),
				request.couponIssueId(),
				request.quantity(),
				originalPrice,
				discountPrice,
				finalPrice
		);
		Order savedOrder = orderRepository.save(order);

		return new OrderCreateResponse(savedOrder.getId());
	}

	public OrderPreviewResponse previewOrder(Long userId, OrderPreviewRequest request) {
		throw new UnsupportedOperationException("주문 미리보기 로직 미구현");
	}

	public OrderDeleteResponse cancelOrder(Long userId, Long orderId) {
		throw new UnsupportedOperationException("주문 삭제 로직 미구현");
	}

	// ===== * private methods * =====
	private BigDecimal calculateDiscountPrice(Long userId, Long couponIssueId, BigDecimal originalPrice) {
		if (couponIssueId == null) {
			return BigDecimal.ZERO;
		}

		CouponIssueRef couponIssueRef = couponIssueRefService.getUsableCouponIssue(userId, couponIssueId);
		CouponMasterRef couponMasterRef = couponMasterRefService.getUsableCouponMaster(couponIssueRef.getCouponMasterId());
		BigDecimal discountPrice = couponMasterRefService.calculateDiscountPrice(originalPrice, couponMasterRef);
		couponIssueRefService.useCouponIssue(userId, couponIssueId);
		return discountPrice;
	}

}
