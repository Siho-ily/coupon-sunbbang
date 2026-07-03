package org.coupon.couponsunbbang.domain.event.entity;

/**
 * ACTIVE - 진행중인 이벤트, 쿠폰 발급 가능
 * ENDED - 종료된 이벤트, 쿠폰 발급 불가
 */
public enum EventStatus {
    ACTIVE,
    ENDED
}
