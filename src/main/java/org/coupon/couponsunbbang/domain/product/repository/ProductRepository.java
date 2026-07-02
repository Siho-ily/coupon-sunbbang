package org.coupon.couponsunbbang.domain.product.repository;

import org.coupon.couponsunbbang.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
