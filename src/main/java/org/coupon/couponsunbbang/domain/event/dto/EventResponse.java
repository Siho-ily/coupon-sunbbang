package org.coupon.couponsunbbang.domain.event.dto;

import lombok.Getter;
import org.coupon.couponsunbbang.domain.event.entity.Event;
import org.coupon.couponsunbbang.domain.event.entity.EventStatus;

import java.time.LocalDateTime;

@Getter
public class EventResponse {
    private Long id;
    private String title;
    private EventStatus status;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private LocalDateTime createdAt;

    // Entity -> DTO 변환
    public EventResponse(Event event) {
        this.id = event.getId();
        this.title = event.getTitle();
        this.status = event.getStatus();
        this.startAt = event.getStartAt();
        this.endAt = event.getEndAt();
        this.createdAt = event.getCreatedAt();
    }
}
