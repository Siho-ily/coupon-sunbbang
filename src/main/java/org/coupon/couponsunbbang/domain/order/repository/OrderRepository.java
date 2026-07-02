package org.coupon.couponsunbbang.domain.order.repository;

import org.coupon.couponsunbbang.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
