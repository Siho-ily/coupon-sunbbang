package org.coupon.couponsunbbang.domain.event.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.coupon.couponsunbbang.domain.event.entity.EventStatus;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class UpdateEventRequest {
    private String title;
    private EventStatus status;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
}
