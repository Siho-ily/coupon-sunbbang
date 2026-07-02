package org.coupon.couponsunbbang.domain.order.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;
import org.coupon.couponsunbbang.domain.order.dto.request.OrderCreateRequest;
import org.coupon.couponsunbbang.domain.order.dto.response.OrderCreateResponse;
import org.coupon.couponsunbbang.domain.order.entity.Order;
import org.coupon.couponsunbbang.domain.order.repository.OrderRepository;
import org.coupon.couponsunbbang.domain.product.entity.Product;
import org.coupon.couponsunbbang.domain.product.exception.ProductNotFoundException;
import org.coupon.couponsunbbang.domain.product.repository.ProductRepository;
import org.coupon.couponsunbbang.global.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
	@Mock
	private OrderRepository orderRepository;

	@Mock
	private ProductRepository productRepository;

	@InjectMocks
	private OrderService orderService;

	@Test
	@DisplayName("쿠폰 없이 주문을 생성하면 상품 가격과 수량으로 최종 금액을 계산해 저장한다")
	void createOrderWithoutCoupon() {
		// given
		Long userId = 1L;
		Long productId = 10L;
		OrderCreateRequest request = new OrderCreateRequest(productId, null, 2);
		Product product = mock(Product.class);

		when(product.getPrice()).thenReturn(new BigDecimal("1000.00"));
		when(productRepository.findById(productId)).thenReturn(Optional.of(product));
		when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
			// mock save에 전달된 Order를 그대로 반환해 JPA 저장 동작을 대신합니다.
			Order order = invocation.getArgument(0);
			ReflectionTestUtils.setField(order, "id", 100L); // JPA 저장 후 생성되는 ID를 대체합니다.
			return order;
		});

		// when
		OrderCreateResponse response = orderService.createOrder(userId, request);

		// then
		// 서비스 내부에서 생성해 save에 넘긴 Order 값을 검증하기 위해 인자를 캡처합니다.
		ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
		assertThat(response.orderId()).isEqualTo(100L);
		verify(orderRepository).save(orderCaptor.capture());

		Order savedOrder = orderCaptor.getValue();
		assertThat(savedOrder.getUserId()).isEqualTo(userId);
		assertThat(savedOrder.getProductId()).isEqualTo(productId);
		assertThat(savedOrder.getCouponIssueId()).isNull();
		assertThat(savedOrder.getQuantity()).isEqualTo(2);
		assertThat(savedOrder.getOriginalPrice()).isEqualByComparingTo("2000.00");
		assertThat(savedOrder.getDiscountPrice()).isEqualByComparingTo("0.00");
		assertThat(savedOrder.getFinalPrice()).isEqualByComparingTo("2000.00");
	}

	@Test
	@DisplayName("존재하지 않는 상품 ID로 주문을 생성하면 상품 없음 예외가 발생한다")
	void createOrderWithNotFoundProduct() {
		// given
		Long userId = 1L;
		Long productId = 999L;
		OrderCreateRequest request = new OrderCreateRequest(productId, null, 1);

		when(productRepository.findById(productId)).thenReturn(Optional.empty());

		// when & then
		ProductNotFoundException exception = assertThrows(
				ProductNotFoundException.class,
				() -> orderService.createOrder(userId, request)
		);

		assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.PRODUCT_NOT_FOUND);
	}
}
