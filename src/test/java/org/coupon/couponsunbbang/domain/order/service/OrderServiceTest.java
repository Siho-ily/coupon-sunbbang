package org.coupon.couponsunbbang.domain.order.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.coupon.couponsunbbang.domain.order.dto.request.OrderCreateRequest;
import org.coupon.couponsunbbang.domain.order.dto.request.OrderPreviewRequest;
import org.coupon.couponsunbbang.domain.order.dto.response.OrderCreateResponse;
import org.coupon.couponsunbbang.domain.order.dto.response.OrderDetailResponse;
import org.coupon.couponsunbbang.domain.order.dto.response.OrderListResponse;
import org.coupon.couponsunbbang.domain.order.dto.response.OrderPreviewResponse;
import org.coupon.couponsunbbang.domain.order.entity.Order;
import org.coupon.couponsunbbang.domain.order.reference.entity.CouponIssueRef;
import org.coupon.couponsunbbang.domain.order.reference.entity.CouponIssueRefStatus;
import org.coupon.couponsunbbang.domain.order.reference.entity.CouponMasterRef;
import org.coupon.couponsunbbang.domain.order.reference.repository.CouponIssueRefRepository;
import org.coupon.couponsunbbang.domain.order.reference.repository.CouponMasterRefRepository;
import org.coupon.couponsunbbang.domain.order.reference.service.CouponIssueRefService;
import org.coupon.couponsunbbang.domain.order.reference.service.CouponMasterRefService;
import org.coupon.couponsunbbang.domain.order.repository.OrderRepository;
import org.coupon.couponsunbbang.domain.product.entity.Product;
import org.coupon.couponsunbbang.domain.product.exception.ProductNotFoundException;
import org.coupon.couponsunbbang.domain.product.repository.ProductRepository;
import org.coupon.couponsunbbang.global.exception.BusinessException;
import org.coupon.couponsunbbang.global.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
	@Mock
	private OrderRepository orderRepository;

	@Mock
	private ProductRepository productRepository;

	@Mock
	private CouponIssueRefRepository couponIssueRefRepository;

	@Mock
	private CouponMasterRefRepository couponMasterRefRepository;

	@InjectMocks
	private CouponIssueRefService couponIssueRefService;

	@InjectMocks
	private CouponMasterRefService couponMasterRefService;

	private OrderService orderService;

	@BeforeEach
	void setUp() {
		orderService = new OrderService(
				orderRepository,
				productRepository,
				couponIssueRefService,
				couponMasterRefService
		);
	}

	@Test
	@DisplayName("주문 목록을 조회하면 본인 주문 목록을 페이지 정보와 함께 반환한다")
	void getOrders() {
		// given
		Long userId = 1L;
		int page = 0;
		int size = 10;
		PageRequest pageRequest = PageRequest.of(page, size);
		Order firstOrder = createOrder(
				100L,
				userId,
				10L,
				null,
				2,
				"2000.00",
				"0.00",
				"2000.00",
				LocalDateTime.of(2026, 7, 3, 10, 0)
		);
		Order secondOrder = createOrder(
				101L,
				userId,
				11L,
				20L,
				1,
				"1000.00",
				"500.00",
				"500.00",
				LocalDateTime.of(2026, 7, 3, 11, 0)
		);

		when(orderRepository.findByUserId(userId, pageRequest)).thenReturn(
				new PageImpl<>(List.of(firstOrder, secondOrder), pageRequest, 2)
		);

		// when
		OrderListResponse response = orderService.getOrders(userId, pageRequest);

		// then
		assertThat(response.page()).isEqualTo(page);
		assertThat(response.size()).isEqualTo(size);
		assertThat(response.orders()).hasSize(2);
		assertThat(response.orders().get(0).orderId()).isEqualTo(100L);
		assertThat(response.orders().get(0).productId()).isEqualTo(10L);
		assertThat(response.orders().get(0).quantity()).isEqualTo(2);
		assertThat(response.orders().get(0).finalPrice()).isEqualByComparingTo("2000.00");
		assertThat(response.orders().get(0).orderedAt()).isEqualTo(LocalDateTime.of(2026, 7, 3, 10, 0));
		assertThat(response.orders().get(1).orderId()).isEqualTo(101L);
		assertThat(response.orders().get(1).productId()).isEqualTo(11L);
		assertThat(response.orders().get(1).quantity()).isEqualTo(1);
		assertThat(response.orders().get(1).finalPrice()).isEqualByComparingTo("500.00");
		assertThat(response.orders().get(1).orderedAt()).isEqualTo(LocalDateTime.of(2026, 7, 3, 11, 0));
	}

	@Test
	@DisplayName("주문 ID로 본인 주문 상세를 조회하면 주문 상세 응답을 반환한다")
	void getOrderDetail() {
		// given
		Long userId = 1L;
		Long orderId = 100L;
		Long couponIssueId = 20L;
		Order order = createOrder(
				orderId,
				userId,
				10L,
				couponIssueId,
				2,
				"2000.00",
				"500.00",
				"1500.00",
				LocalDateTime.of(2026, 7, 3, 10, 0)
		);

		when(orderRepository.findByIdAndUserId(orderId, userId)).thenReturn(Optional.of(order));

		// when
		OrderDetailResponse response = orderService.getOrderDetail(userId, orderId);

		// then
		assertThat(response.orderId()).isEqualTo(orderId);
		assertThat(response.userId()).isEqualTo(userId);
		assertThat(response.productId()).isEqualTo(10L);
		assertThat(response.couponIssueId()).isEqualTo(couponIssueId);
		assertThat(response.quantity()).isEqualTo(2);
		assertThat(response.originalPrice()).isEqualByComparingTo("2000.00");
		assertThat(response.discountPrice()).isEqualByComparingTo("500.00");
		assertThat(response.finalPrice()).isEqualByComparingTo("1500.00");
		assertThat(response.orderedAt()).isEqualTo(LocalDateTime.of(2026, 7, 3, 10, 0));
	}

	@Test
	@DisplayName("본인 주문이 아니거나 존재하지 않는 주문 ID로 상세 조회하면 주문 없음 예외가 발생한다")
	void getOrderDetailWithNotFoundOrder() {
		// given
		Long userId = 1L;
		Long orderId = 999L;

		when(orderRepository.findByIdAndUserId(orderId, userId)).thenReturn(Optional.empty());

		// when & then
		BusinessException exception = assertThrows(
				BusinessException.class,
				() -> orderService.getOrderDetail(userId, orderId)
		);

		assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.RESOURCE_NOT_FOUND);
	}

	@Test
	@DisplayName("쿠폰 없이 주문을 생성하면 상품 가격과 수량으로 최종 금액을 계산해 저장한다")
	void createOrderWithoutCoupon() {
		// given
		Long userId = 1L;
		Long productId = 10L;
		OrderCreateRequest request = new OrderCreateRequest(productId, null, 2);
		Product product = createProduct(productId, "테스트 상품", "1000.00");

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
	@DisplayName("정액 쿠폰으로 주문을 생성하면 할인 금액을 차감해 저장하고 쿠폰을 사용 처리한다")
	void createOrderWithFixedCoupon() {
		// given
		Long userId = 1L;
		Long productId = 10L;
		Long couponIssueId = 20L;
		Long couponMasterId = 30L;
		OrderCreateRequest request = new OrderCreateRequest(productId, couponIssueId, 2);
		Product product = createProduct(productId, "테스트 상품", "1000.00");
		CouponIssueRef couponIssueRef = createCouponIssueRef(
				couponIssueId,
				userId,
				couponMasterId,
				CouponIssueRefStatus.UNUSED
		);
		CouponMasterRef couponMasterRef = createCouponMasterRef(
				couponMasterId,
				"FIXED",
				"500.00",
				LocalDateTime.of(2026, 7, 31, 23, 59)
		);

		when(productRepository.findById(productId)).thenReturn(Optional.of(product));
		when(couponIssueRefRepository.findByIdAndUserId(couponIssueId, userId)).thenReturn(Optional.of(couponIssueRef));
		when(couponMasterRefRepository.findById(couponMasterId)).thenReturn(Optional.of(couponMasterRef));
		when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
			Order order = invocation.getArgument(0);
			ReflectionTestUtils.setField(order, "id", 100L);
			return order;
		});

		// when
		OrderCreateResponse response = orderService.createOrder(userId, request);

		// then
		ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
		assertThat(response.orderId()).isEqualTo(100L);
		verify(orderRepository).save(orderCaptor.capture());

		Order savedOrder = orderCaptor.getValue();
		assertThat(savedOrder.getCouponIssueId()).isEqualTo(couponIssueId);
		assertThat(savedOrder.getOriginalPrice()).isEqualByComparingTo("2000.00");
		assertThat(savedOrder.getDiscountPrice()).isEqualByComparingTo("500.00");
		assertThat(savedOrder.getFinalPrice()).isEqualByComparingTo("1500.00");
		assertThat(couponIssueRef.getStatus()).isEqualTo(CouponIssueRefStatus.USED);
		assertThat(couponIssueRef.getUsedAt()).isNotNull();
	}

	@Test
	@DisplayName("정률 쿠폰으로 주문을 생성하면 할인율만큼 할인 금액을 계산해 저장한다")
	void createOrderWithRateCoupon() {
		// given
		Long userId = 1L;
		Long productId = 10L;
		Long couponIssueId = 20L;
		Long couponMasterId = 30L;
		OrderCreateRequest request = new OrderCreateRequest(productId, couponIssueId, 3);
		Product product = createProduct(productId, "테스트 상품", "1000.00");
		CouponIssueRef couponIssueRef = createCouponIssueRef(
				couponIssueId,
				userId,
				couponMasterId,
				CouponIssueRefStatus.UNUSED
		);
		CouponMasterRef couponMasterRef = createCouponMasterRef(
				couponMasterId,
				"RATE",
				"10.00",
				LocalDateTime.of(2026, 7, 31, 23, 59)
		);

		when(productRepository.findById(productId)).thenReturn(Optional.of(product));
		when(couponIssueRefRepository.findByIdAndUserId(couponIssueId, userId)).thenReturn(Optional.of(couponIssueRef));
		when(couponMasterRefRepository.findById(couponMasterId)).thenReturn(Optional.of(couponMasterRef));
		when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
			Order order = invocation.getArgument(0);
			ReflectionTestUtils.setField(order, "id", 100L);
			return order;
		});

		// when
		OrderCreateResponse response = orderService.createOrder(userId, request);

		// then
		ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
		assertThat(response.orderId()).isEqualTo(100L);
		verify(orderRepository).save(orderCaptor.capture());

		Order savedOrder = orderCaptor.getValue();
		assertThat(savedOrder.getOriginalPrice()).isEqualByComparingTo("3000.00");
		assertThat(savedOrder.getDiscountPrice()).isEqualByComparingTo("300.00");
		assertThat(savedOrder.getFinalPrice()).isEqualByComparingTo("2700.00");
		assertThat(couponIssueRef.getStatus()).isEqualTo(CouponIssueRefStatus.USED);
		assertThat(couponIssueRef.getUsedAt()).isNotNull();
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

	@Test
	@DisplayName("쿠폰 없이 주문을 미리보기하면 상품 가격과 수량으로 예상 금액을 반환한다")
	void previewOrderWithoutCoupon() {
		// given
		Long userId = 1L;
		Long productId = 10L;
		OrderPreviewRequest request = new OrderPreviewRequest(productId, null, 2);
		Product product = createProduct(productId, "테스트 상품", "1000.00");

		when(productRepository.findById(productId)).thenReturn(Optional.of(product));

		// when
		OrderPreviewResponse response = orderService.previewOrder(userId, request);

		// then
		assertThat(response.productId()).isEqualTo(productId);
		assertThat(response.couponIssueId()).isNull();
		assertThat(response.quantity()).isEqualTo(2);
		assertThat(response.originalPrice()).isEqualByComparingTo("2000.00");
		assertThat(response.discountPrice()).isEqualByComparingTo("0.00");
		assertThat(response.finalPrice()).isEqualByComparingTo("2000.00");
		verify(orderRepository, never()).save(any(Order.class));
	}

	@Test
	@DisplayName("정액 쿠폰으로 주문을 미리보기하면 할인 금액을 차감한 예상 금액을 반환하고 쿠폰은 사용 처리하지 않는다")
	void previewOrderWithFixedCoupon() {
		// given
		Long userId = 1L;
		Long productId = 10L;
		Long couponIssueId = 20L;
		Long couponMasterId = 30L;
		OrderPreviewRequest request = new OrderPreviewRequest(productId, couponIssueId, 2);
		Product product = createProduct(productId, "테스트 상품", "1000.00");
		CouponIssueRef couponIssueRef = createCouponIssueRef(
				couponIssueId,
				userId,
				couponMasterId,
				CouponIssueRefStatus.UNUSED
		);
		CouponMasterRef couponMasterRef = createCouponMasterRef(
				couponMasterId,
				"FIXED",
				"500.00",
				LocalDateTime.of(2026, 7, 31, 23, 59)
		);

		when(productRepository.findById(productId)).thenReturn(Optional.of(product));
		when(couponIssueRefRepository.findByIdAndUserId(couponIssueId, userId)).thenReturn(Optional.of(couponIssueRef));
		when(couponMasterRefRepository.findById(couponMasterId)).thenReturn(Optional.of(couponMasterRef));

		// when
		OrderPreviewResponse response = orderService.previewOrder(userId, request);

		// then
		assertThat(response.productId()).isEqualTo(productId);
		assertThat(response.couponIssueId()).isEqualTo(couponIssueId);
		assertThat(response.quantity()).isEqualTo(2);
		assertThat(response.originalPrice()).isEqualByComparingTo("2000.00");
		assertThat(response.discountPrice()).isEqualByComparingTo("500.00");
		assertThat(response.finalPrice()).isEqualByComparingTo("1500.00");
		assertThat(couponIssueRef.getStatus()).isEqualTo(CouponIssueRefStatus.UNUSED);
		assertThat(couponIssueRef.getUsedAt()).isNull();
		verify(orderRepository, never()).save(any(Order.class));
	}

	@Test
	@DisplayName("정률 쿠폰으로 주문을 미리보기하면 할인율만큼 계산한 예상 금액을 반환하고 쿠폰은 사용 처리하지 않는다")
	void previewOrderWithRateCoupon() {
		// given
		Long userId = 1L;
		Long productId = 10L;
		Long couponIssueId = 20L;
		Long couponMasterId = 30L;
		OrderPreviewRequest request = new OrderPreviewRequest(productId, couponIssueId, 3);
		Product product = createProduct(productId, "테스트 상품", "1000.00");
		CouponIssueRef couponIssueRef = createCouponIssueRef(
				couponIssueId,
				userId,
				couponMasterId,
				CouponIssueRefStatus.UNUSED
		);
		CouponMasterRef couponMasterRef = createCouponMasterRef(
				couponMasterId,
				"RATE",
				"10.00",
				LocalDateTime.of(2026, 7, 31, 23, 59)
		);

		when(productRepository.findById(productId)).thenReturn(Optional.of(product));
		when(couponIssueRefRepository.findByIdAndUserId(couponIssueId, userId)).thenReturn(Optional.of(couponIssueRef));
		when(couponMasterRefRepository.findById(couponMasterId)).thenReturn(Optional.of(couponMasterRef));

		// when
		OrderPreviewResponse response = orderService.previewOrder(userId, request);

		// then
		assertThat(response.productId()).isEqualTo(productId);
		assertThat(response.couponIssueId()).isEqualTo(couponIssueId);
		assertThat(response.quantity()).isEqualTo(3);
		assertThat(response.originalPrice()).isEqualByComparingTo("3000.00");
		assertThat(response.discountPrice()).isEqualByComparingTo("300.00");
		assertThat(response.finalPrice()).isEqualByComparingTo("2700.00");
		assertThat(couponIssueRef.getStatus()).isEqualTo(CouponIssueRefStatus.UNUSED);
		assertThat(couponIssueRef.getUsedAt()).isNull();
		verify(orderRepository, never()).save(any(Order.class));
	}

	private Product createProduct(Long productId, String title, String price) {
		Product product = new Product();
		ReflectionTestUtils.setField(product, "id", productId);
		ReflectionTestUtils.setField(product, "title", title);
		ReflectionTestUtils.setField(product, "price", new BigDecimal(price));
		ReflectionTestUtils.setField(product, "createdAt", LocalDateTime.of(2026, 7, 2, 10, 0));
		return product;
	}

	private Order createOrder(
			Long orderId,
			Long userId,
			Long productId,
			Long couponIssueId,
			Integer quantity,
			String originalPrice,
			String discountPrice,
			String finalPrice,
			LocalDateTime createdAt
	) {
		Order order = Order.create(
				userId,
				productId,
				couponIssueId,
				quantity,
				new BigDecimal(originalPrice),
				new BigDecimal(discountPrice),
				new BigDecimal(finalPrice)
		);
		ReflectionTestUtils.setField(order, "id", orderId);
		ReflectionTestUtils.setField(order, "createdAt", createdAt);
		return order;
	}

	private CouponIssueRef createCouponIssueRef(
			Long couponIssueId,
			Long userId,
			Long couponMasterId,
			CouponIssueRefStatus status
	) {
		CouponIssueRef couponIssueRef = new CouponIssueRef();
		ReflectionTestUtils.setField(couponIssueRef, "id", couponIssueId);
		ReflectionTestUtils.setField(couponIssueRef, "userId", userId);
		ReflectionTestUtils.setField(couponIssueRef, "couponMasterId", couponMasterId);
		ReflectionTestUtils.setField(couponIssueRef, "status", status);
		ReflectionTestUtils.setField(couponIssueRef, "createdAt", LocalDateTime.of(2026, 7, 2, 10, 0));
		return couponIssueRef;
	}

	private CouponMasterRef createCouponMasterRef(
			Long couponMasterId,
			String discountType,
			String discountValue,
			LocalDateTime useExpiredAt
	) {
		CouponMasterRef couponMasterRef = new CouponMasterRef();
		ReflectionTestUtils.setField(couponMasterRef, "id", couponMasterId);
		ReflectionTestUtils.setField(couponMasterRef, "eventId", 1L);
		ReflectionTestUtils.setField(couponMasterRef, "title", "테스트 쿠폰");
		ReflectionTestUtils.setField(couponMasterRef, "discountType", discountType);
		ReflectionTestUtils.setField(couponMasterRef, "discountValue", new BigDecimal(discountValue));
		ReflectionTestUtils.setField(couponMasterRef, "totalQuantity", 100);
		ReflectionTestUtils.setField(couponMasterRef, "issueExpiredAt", LocalDateTime.of(2026, 7, 31, 23, 59));
		ReflectionTestUtils.setField(couponMasterRef, "useExpiredAt", useExpiredAt);
		return couponMasterRef;
	}
}
