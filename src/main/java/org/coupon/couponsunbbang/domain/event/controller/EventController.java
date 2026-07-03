package org.coupon.couponsunbbang.domain.event.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.coupon.couponsunbbang.domain.event.dto.CreateEventRequest;
import org.coupon.couponsunbbang.domain.event.dto.EventResponse;
import org.coupon.couponsunbbang.domain.event.dto.UpdateEventRequest;
import org.coupon.couponsunbbang.domain.event.entity.EventStatus;
import org.coupon.couponsunbbang.domain.event.service.EventService;
import org.coupon.couponsunbbang.global.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    // Entity를 Service에 위임
    // 생성은 Service에서 Builder로 처리

    // 이벤트 등록
    @PostMapping
    public ResponseEntity<ApiResponse<EventResponse>> createEvent(@Valid @RequestBody CreateEventRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        eventService.createEvent(
                                request.getTitle(),
                                request.getStatus(),
                                request.getStartAt(),
                                request.getEndAt()
                        ))
                );
    }

    // 이벤트 목록 조회
    @GetMapping
    public ResponseEntity<ApiResponse<List<EventResponse>>> getEvents(
            @RequestParam(required = false) EventStatus status) {
        return ResponseEntity.ok(ApiResponse.success(eventService.getEvents(status)));
    }

    // 이벤트 단건 조회
    @GetMapping("/{eventId}")
    public ResponseEntity<ApiResponse<EventResponse>> getEvent(@PathVariable Long eventId) {
        return ResponseEntity.ok(ApiResponse.success(eventService.getEvent(eventId)));
    }

    // 이벤트 수정
    @PutMapping("/{eventId}")
    public ResponseEntity<ApiResponse<EventResponse>> updateEvent(
            @PathVariable Long eventId,
            @Valid @RequestBody UpdateEventRequest request) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        eventService.updateEvent(
                                eventId,
                                request.getTitle(),
                                request.getStatus(),
                                request.getStartAt(),
                                request.getEndAt()
                        )
                )
        );
    }

    // 이벤트 삭제 - Soft Delete
    @DeleteMapping("/{eventId}")
    public ResponseEntity<ApiResponse<Void>> deleteEvent(@PathVariable Long eventId) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}