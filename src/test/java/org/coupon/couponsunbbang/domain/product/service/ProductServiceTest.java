package org.coupon.couponsunbbang.domain.product.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.coupon.couponsunbbang.domain.product.dto.response.ProductListResponse;
import org.coupon.couponsunbbang.domain.product.dto.response.ProductResponse;
import org.coupon.couponsunbbang.domain.product.entity.Product;
import org.coupon.couponsunbbang.domain.product.exception.ProductNotFoundException;
import org.coupon.couponsunbbang.domain.product.repository.ProductRepository;
import org.coupon.couponsunbbang.global.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
	@Mock
	private ProductRepository productRepository;

	@InjectMocks
	private ProductService productService;

	@Test
	@DisplayName("상품 목록을 조회하면 상품 응답 목록을 반환한다")
	void getProducts() {
		// given
		int page = 0;
		int size = 10;
		Product firstProduct = createProduct(1L, "테스트 상품 1", "1000.00", LocalDateTime.of(2026, 7, 2, 10, 0));
		Product secondProduct = createProduct(2L, "테스트 상품 2", "2000.00", LocalDateTime.of(2026, 7, 2, 11, 0));
		PageRequest pageRequest = PageRequest.of(page, size);

		when(productRepository.findAll(pageRequest)).thenReturn(
				new PageImpl<>(List.of(firstProduct, secondProduct), pageRequest, 12)
		);

		// when
		ProductListResponse response = productService.getProducts(page, size);

		// then
		assertThat(response.products()).hasSize(2);
		assertThat(response.products().get(0).productId()).isEqualTo(1L);
		assertThat(response.products().get(0).title()).isEqualTo("테스트 상품 1");
		assertThat(response.products().get(0).price()).isEqualByComparingTo("1000.00");
		assertThat(response.products().get(1).productId()).isEqualTo(2L);
		assertThat(response.page()).isEqualTo(0);
		assertThat(response.size()).isEqualTo(10);
		assertThat(response.totalElements()).isEqualTo(12);
		assertThat(response.totalPages()).isEqualTo(2);
		assertThat(response.hasNext()).isTrue();
	}

	@Test
	@DisplayName("상품 ID로 단건 조회하면 상품 응답을 반환한다")
	void getProductDetail() {
		// given
		Long productId = 1L;
		LocalDateTime createdAt = LocalDateTime.of(2026, 7, 2, 10, 0);
		Product product = createProduct(productId, "테스트 상품", "1000.00", createdAt);

		when(productRepository.findById(productId)).thenReturn(Optional.of(product));

		// when
		ProductResponse response = productService.getProductDetail(productId);

		// then
		assertThat(response.productId()).isEqualTo(productId);
		assertThat(response.title()).isEqualTo("테스트 상품");
		assertThat(response.price()).isEqualByComparingTo("1000.00");
		assertThat(response.createdAt()).isEqualTo(createdAt);
	}

	@Test
	@DisplayName("존재하지 않는 상품 ID로 단건 조회하면 상품 없음 예외가 발생한다")
	void getProductDetailWithNotFoundProduct() {
		// given
		Long productId = 999L;

		when(productRepository.findById(productId)).thenReturn(Optional.empty());

		// when & then
		ProductNotFoundException exception = assertThrows(
				ProductNotFoundException.class,
				() -> productService.getProductDetail(productId)
		);

		assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.PRODUCT_NOT_FOUND);
	}

	private Product createProduct(Long productId, String title, String price, LocalDateTime createdAt) {
		Product product = new Product();
		ReflectionTestUtils.setField(product, "id", productId);
		ReflectionTestUtils.setField(product, "title", title);
		ReflectionTestUtils.setField(product, "price", new BigDecimal(price));
		ReflectionTestUtils.setField(product, "createdAt", createdAt);
		return product;
	}
}
