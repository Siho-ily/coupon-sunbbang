package org.coupon.couponsunbbang.domain.product.controller;

import lombok.RequiredArgsConstructor;
import org.coupon.couponsunbbang.domain.product.dto.response.ProductListResponse;
import org.coupon.couponsunbbang.domain.product.dto.response.ProductResponse;
import org.coupon.couponsunbbang.domain.product.service.ProductService;
import org.coupon.couponsunbbang.global.common.ApiResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
	private final ProductService productService;

	@GetMapping
	public ResponseEntity<ApiResponse<ProductListResponse>> getProducts(
			@PageableDefault(page = 0, size = 10) Pageable pageable
	) {
		ProductListResponse response = productService.getProducts(pageable);
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
