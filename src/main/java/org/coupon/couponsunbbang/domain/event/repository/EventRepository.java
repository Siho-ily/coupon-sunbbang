package org.coupon.couponsunbbang.domain.event.repository;

import org.coupon.couponsunbbang.domain.event.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
    // QueryDSL은 EventQueryRepository에서 처리
}