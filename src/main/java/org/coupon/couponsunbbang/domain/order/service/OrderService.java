package org.coupon.couponsunbbang.domain.order.service;

import lombok.RequiredArgsConstructor;
import org.coupon.couponsunbbang.domain.order.dto.request.OrderCreateRequest;
import org.coupon.couponsunbbang.domain.order.dto.request.OrderPreviewRequest;
import org.coupon.couponsunbbang.domain.order.dto.response.OrderCreateResponse;
import org.coupon.couponsunbbang.domain.order.dto.response.OrderDeleteResponse;
import org.coupon.couponsunbbang.domain.order.dto.response.OrderDetailResponse;
import org.coupon.couponsunbbang.domain.order.dto.response.OrderListResponse;
import org.coupon.couponsunbbang.domain.order.dto.response.OrderPreviewResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
	public OrderListResponse getOrders(Long userId, int page, int size) {
		throw new UnsupportedOperationException("주문 목록 조회 로직 미구현");
	}

	public OrderDetailResponse getOrderDetail(Long userId, Long orderId) {
		throw new UnsupportedOperationException("주문 상세 조회 로직 미구현");
	}

	public OrderCreateResponse createOrder(Long userId, OrderCreateRequest request) {
		throw new UnsupportedOperationException("주문 생성 로직 미구현");
	}

	public OrderPreviewResponse previewOrder(Long userId, OrderPreviewRequest request) {
		throw new UnsupportedOperationException("주문 미리보기 로직 미구현");
	}

	public OrderDeleteResponse cancelOrder(Long userId, Long orderId) {
		throw new UnsupportedOperationException("주문 삭제 로직 미구현");
	}
}
