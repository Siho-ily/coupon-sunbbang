package org.coupon.couponsunbbang.domain.order.controller;

import lombok.RequiredArgsConstructor;
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
		public ResponseEntity<ApiResponse<?>> getOrders(
						@RequestParam Long userId, // 임시
						@RequestParam(defaultValue = "0") int page,
						@RequestParam(defaultValue = "10") int size
		) {
				return ResponseEntity
								       .status(HttpStatus.OK)
								       .body(ApiResponse.success(null));
		}


		@GetMapping("/{orderId}")
		public ResponseEntity<ApiResponse<?>> getOrderDetailById(
						@RequestParam Long userId, // 임시
						@PathVariable Long orderId
		) {
				return ResponseEntity
								       .status(HttpStatus.OK)
								       .body(ApiResponse.success(null));
		}


		@PostMapping
		public ResponseEntity<ApiResponse<?>> createOrder(
						@RequestParam Long userId // 임시
		) {
				return ResponseEntity
								       .status(HttpStatus.CREATED)
								       .body(ApiResponse.success(null));
		}


		@PostMapping("/preview")
		public ResponseEntity<ApiResponse<?>> previewOrders(
						@RequestParam Long userId // 임시
		) {
				return ResponseEntity
								       .status(HttpStatus.OK)
								       .body(ApiResponse.success(null));
		}


		@DeleteMapping("/{orderId}")
		public ResponseEntity<ApiResponse<?>> cancelOrders(
						@RequestParam Long userId, // 임시
						@PathVariable Long orderId
		) {
				return ResponseEntity
								       .status(HttpStatus.OK)
								       .body(ApiResponse.success(null));
		}
}
