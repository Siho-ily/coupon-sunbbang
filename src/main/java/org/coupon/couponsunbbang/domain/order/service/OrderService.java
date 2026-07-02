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

	public OrderListResponse getOrders(Long userId, int page, int size) {
		throw new UnsupportedOperationException("주문 목록 조회 로직 미구현");
	}

	public OrderDetailResponse getOrderDetail(Long userId, Long orderId) {
		throw new UnsupportedOperationException("주문 상세 조회 로직 미구현");
	}

	@Transactional
	public OrderCreateResponse createOrder(Long userId, OrderCreateRequest request) {
		if (request.couponIssueId() != null) {
			throw new UnsupportedOperationException("쿠폰 적용 주문 생성 로직 미구현");
		}

		// 상품 조회
		Product product = productRepository.findById(request.productId())
				.orElseThrow(ProductNotFoundException::new);

		// 금액 계산
		BigDecimal originalPrice = product.getPrice().multiply(BigDecimal.valueOf(request.quantity()));
		BigDecimal discountPrice = BigDecimal.ZERO; // 현재 쿠폰 미적용 상태이므로, 할인 금액은 무조건 0원 입니다.
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
}
