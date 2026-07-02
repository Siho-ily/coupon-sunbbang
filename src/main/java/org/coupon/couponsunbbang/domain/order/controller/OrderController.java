package org.coupon.couponsunbbang.domain.order.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.coupon.couponsunbbang.domain.order.dto.request.OrderCreateRequest;
import org.coupon.couponsunbbang.domain.order.dto.request.OrderPreviewRequest;
import org.coupon.couponsunbbang.domain.order.dto.response.OrderCreateResponse;
import org.coupon.couponsunbbang.domain.order.dto.response.OrderDeleteResponse;
import org.coupon.couponsunbbang.domain.order.dto.response.OrderDetailResponse;
import org.coupon.couponsunbbang.domain.order.dto.response.OrderListResponse;
import org.coupon.couponsunbbang.domain.order.dto.response.OrderPreviewResponse;
import org.coupon.couponsunbbang.domain.order.service.OrderService;
import org.coupon.couponsunbbang.global.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
	private final OrderService orderService;

	@GetMapping
	public ResponseEntity<ApiResponse<OrderListResponse>> getOrders(
			@RequestParam Long userId, // 임시
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size
	) {
		OrderListResponse response = orderService.getOrders(userId, page, size);
		return ResponseEntity
				       .status(HttpStatus.OK)
				       .body(ApiResponse.success(response));
	}


	@GetMapping("/{orderId}")
	public ResponseEntity<ApiResponse<OrderDetailResponse>> getOrderDetailById(
			@RequestParam Long userId, // 임시
			@PathVariable Long orderId
	) {
		OrderDetailResponse response = orderService.getOrderDetail(userId, orderId);
		return ResponseEntity
				       .status(HttpStatus.OK)
				       .body(ApiResponse.success(response));
	}


	@PostMapping
	public ResponseEntity<ApiResponse<OrderCreateResponse>> createOrder(
			@RequestParam Long userId, // 임시
			@Valid @RequestBody OrderCreateRequest request
	) {
		OrderCreateResponse response = orderService.createOrder(userId, request);
		return ResponseEntity
				       .status(HttpStatus.CREATED)
				       .body(ApiResponse.success(response));
	}


	@PostMapping("/preview")
	public ResponseEntity<ApiResponse<OrderPreviewResponse>> previewOrders(
			@RequestParam Long userId, // 임시
			@Valid @RequestBody OrderPreviewRequest request
	) {
		OrderPreviewResponse response = orderService.previewOrder(userId, request);
		return ResponseEntity
				       .status(HttpStatus.OK)
				       .body(ApiResponse.success(response));
	}


	@DeleteMapping("/{orderId}")
	public ResponseEntity<ApiResponse<OrderDeleteResponse>> cancelOrders(
			@RequestParam Long userId, // 임시
			@PathVariable Long orderId
	) {
		OrderDeleteResponse response = orderService.cancelOrder(userId, orderId);
		return ResponseEntity
				       .status(HttpStatus.OK)
				       .body(ApiResponse.success(response));
	}
}
