package org.coupon.couponsunbbang.domain.order.service;

import lombok.RequiredArgsConstructor;
import org.coupon.couponsunbbang.domain.order.dto.request.OrderCreateRequest;
import org.coupon.couponsunbbang.domain.order.dto.request.OrderPreviewRequest;
import org.coupon.couponsunbbang.domain.order.dto.response.OrderCreateResponse;
import org.coupon.couponsunbbang.domain.order.dto.response.OrderPreviewResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
	public OrderCreateResponse createOrder(Long userId, OrderCreateRequest request) {
		throw new UnsupportedOperationException("주문 생성 로직 미구현");
	}

	public OrderPreviewResponse previewOrder(Long userId, OrderPreviewRequest request) {
		throw new UnsupportedOperationException("주문 미리보기 로직 미구현");
	}
}
