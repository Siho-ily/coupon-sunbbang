package org.coupon.couponsunbbang.domain.product.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.coupon.couponsunbbang.domain.product.dto.response.ProductListResponse;
import org.coupon.couponsunbbang.domain.product.dto.response.ProductResponse;
import org.coupon.couponsunbbang.domain.product.entity.Product;
import org.coupon.couponsunbbang.domain.product.exception.ProductNotFoundException;
import org.coupon.couponsunbbang.domain.product.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {
	private final ProductRepository productRepository;

	public ProductListResponse getProducts(int page, int size) {
		Page<Product> productPage = productRepository.findAll(PageRequest.of(page, size));
		// Page<Product>에서 Product 리스트만 꺼내 List<ProductResponse>로 변환
		List<ProductResponse> products = productPage.getContent()
				.stream()
				.map(ProductResponse::from)
				.toList();

		return new ProductListResponse(
				products,
				productPage.getNumber(),
				productPage.getSize(),
				productPage.getTotalElements(),
				productPage.getTotalPages(),
				productPage.hasNext()
		);
	}

	public ProductResponse getProductDetail(Long productId) {
		Product product = productRepository.findById(productId)
				.orElseThrow(ProductNotFoundException::new);

		return ProductResponse.from(product);
	}
}
