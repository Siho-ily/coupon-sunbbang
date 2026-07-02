package org.coupon.couponsunbbang.domain.product.controller;

import lombok.RequiredArgsConstructor;
import org.coupon.couponsunbbang.domain.product.dto.response.ProductListResponse;
import org.coupon.couponsunbbang.domain.product.dto.response.ProductResponse;
import org.coupon.couponsunbbang.domain.product.service.ProductService;
import org.coupon.couponsunbbang.global.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
	private final ProductService productService;

	@GetMapping
	public ResponseEntity<ApiResponse<ProductListResponse>> getProducts(
			@RequestParam(defaultValue = "0") int page,   // Spring Data는 관례에 의해 0-based를 사용하고 있어 기본값은 0입니다.
			@RequestParam(defaultValue = "10") int size
	) {
		ProductListResponse response = productService.getProducts(page, size);
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(ApiResponse.success(response));
	}

	@GetMapping("/{productId}")
	public ResponseEntity<ApiResponse<ProductResponse>> getProductDetail(
			@PathVariable Long productId
	) {
		ProductResponse response = productService.getProductDetail(productId);
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(ApiResponse.success(response));
	}
}
