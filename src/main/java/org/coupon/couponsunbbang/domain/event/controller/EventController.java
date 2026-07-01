package org.coupon.couponsunbbang.domain.event.controller;

import lombok.RequiredArgsConstructor;
import org.coupon.couponsunbbang.domain.event.dto.CreatEventRequest;
import org.coupon.couponsunbbang.domain.event.dto.EventResponse;
import org.coupon.couponsunbbang.domain.event.dto.UpdateEventRequest;
import org.coupon.couponsunbbang.domain.event.entity.EventStatus;
import org.coupon.couponsunbbang.domain.event.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    // Entity를 Service에 위임
    // 생성은 Service에서 Builder로 처리

    // 이벤트 등록
    @PostMapping
    public ResponseEntity<EventResponse> createEvent(@RequestBody CreatEventRequest request) {
        return ResponseEntity.ok(
                new EventResponse(eventService.createEvent(
                        request.getTitle(),
                        request.getStatus(),
                        request.getStartAt(),
                        request.getEndAt()
                ))
        );
    }

    // 이벤트 목록 조회
    @GetMapping
    public ResponseEntity<List<EventResponse>> getEvents(
            @RequestParam(required = false) EventStatus status) {
        List<EventResponse> responses = eventService.getEvents(status)
                .stream()
                .map(EventResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    // 이벤트 단건 조회
    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponse> getEvent(@PathVariable Long eventId) {
        return ResponseEntity.ok(new EventResponse(eventService.getEvent(eventId)));
    }

    // 이벤트 수정
    @PutMapping("/{eventId}")
    public ResponseEntity<EventResponse> updateEvent(
            @PathVariable Long eventId,
            @RequestBody UpdateEventRequest request) {
        return ResponseEntity.ok(
                new EventResponse(eventService.updateEvent(
                        eventId,
                        request.getTitle(),
                        request.getStatus(),
                        request.getStartAt(),
                        request.getEndAt()
                ))
        );
    }

    // 이벤트 삭제 - Soft Delete
    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long eventId) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.noContent().build();
    }
}
