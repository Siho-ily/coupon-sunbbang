package org.coupon.couponsunbbang.domain.event.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.coupon.couponsunbbang.domain.event.entity.Event;
import org.coupon.couponsunbbang.domain.event.entity.EventStatus;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class EventResponse {
    private Long id;
    private String title;
    private EventStatus status;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private LocalDateTime createdAt;

    // 정적 팩토리 메서드 (Entity -> DTO 변환)
    public static EventResponse from(Event event) {
        EventResponse response = new EventResponse();
        response.id = event.getId();
        response.title = event.getTitle();
        response.status = event.getStatus();
        response.startAt = event.getStartAt();
        response.endAt = event.getEndAt();
        response.createdAt = event.getCreatedAt();
        return response;
    }
}