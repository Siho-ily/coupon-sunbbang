package org.coupon.couponsunbbang.domain.event.service;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.coupon.couponsunbbang.domain.event.dto.EventResponse;
import org.coupon.couponsunbbang.domain.event.entity.Event;
import org.coupon.couponsunbbang.domain.event.entity.EventStatus;
import org.coupon.couponsunbbang.domain.event.repository.EventRepository;
import org.coupon.couponsunbbang.global.exception.BusinessException;
import org.coupon.couponsunbbang.global.exception.ErrorCode;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventService {

    private final EventRepository eventRepository;

    // 이벤트 등록 POST /api/v1/events
    // Controller에서 위임 받아 Builder 패턴으로 Event 객체 생성
    // new Event() 직접 호출 대신 Builder 사용 (protected 제한으로 외부 생성 불가)
    @Transactional
    public EventResponse createEvent(String title, EventStatus status, LocalDateTime startAt, LocalDateTime endAt) {
        Event event = Event.builder()
                .title(title)
                .status(status)
                .startAt(startAt)
                .endAt(endAt)
                .build();

        return EventResponse.from(eventRepository.save(event));
    }

    // 이벤트 목록 조회 GET /api/v1/events?status=
    // status가 null이면 전체 조회, 있으면 해당 상태로 필터링
    public List<EventResponse> getEvents(EventStatus status) {
        if (status == null) {
            return eventRepository.findAllNotDeleted()
                    .stream()
                    .map(EventResponse::from)
                    .collect(Collectors.toList());
        }
        return eventRepository.findByStatusAndNotDeleted(status)
                .stream()
                .map(EventResponse::from)
                .collect(Collectors.toList());
    }

    // 이벤트 단건 조회 GET /api/v1/events/{eventId}
    public EventResponse getEvent(Long eventId) {
        return EventResponse.from(eventRepository.findByIdAndNotDeleted(eventId)
                .orElseThrow(() -> new BusinessException(ErrorCode.EVENT_NOT_FOUND)));
    }

    // 이벤트 수정 PUT /api/v1/events/{eventId}
    @Transactional
    public EventResponse updateEvent(Long eventId, String title, EventStatus status, LocalDateTime startAt, LocalDateTime endAt) {
        Event event = eventRepository.findByIdAndNotDeleted(eventId)
                .orElseThrow(() -> new BusinessException(ErrorCode.EVENT_NOT_FOUND));

        event.update(title, status, startAt, endAt);
        return EventResponse.from(event);
    }

    // 이벤트 삭제 (Soft Delete) DELETE /api/v1/events/{eventId}
    @Transactional
    public void deleteEvent(Long eventId) {
        Event event = eventRepository.findByIdAndNotDeleted(eventId)
                .orElseThrow(() -> new BusinessException(ErrorCode.EVENT_NOT_FOUND));

        event.delete();
    }
}