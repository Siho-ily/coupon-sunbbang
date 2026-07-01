package org.coupon.couponsunbbang.domain.event.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.coupon.couponsunbbang.domain.event.entity.Event;
import org.coupon.couponsunbbang.domain.event.entity.EventStatus;
import org.coupon.couponsunbbang.domain.event.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    // 이벤트 등록 POST /api/v1/events
    // Controller에서 위임 받아 Builder 패턴으로 Event 객체 생성
    // new Event() 직접 호출 대신 Builder 사용 (protected 제한으로 외부 생성 불가)
    @Transactional
    public Event createEvent(String title, EventStatus status, LocalDateTime startAt, LocalDateTime endAt) {
        Event event = Event.builder()
                .title(title)
                .status(status)
                .startAt(startAt)
                .endAt(endAt)
                .build();

        return eventRepository.save(event);
    }

    // 이벤트 목록 조회 GET /api/v1/events?status=
    // status가 null이면 전체 조회, 있으면 해당 상태로 필터링
    public List<Event> getEvents(EventStatus status) {
        if (status == null) {
            return eventRepository.findAllNotDeleted();
        }
        return eventRepository.findByStatusAndNotDeleted(status);
    }

    // 이벤트 단건 조회 GET /api/v1/events/{eventId}
    public Event getEvent(Long eventId) {
        return eventRepository.findByIdAndNotDeleted(eventId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이벤트 입니다. id=" + eventId));
    }

    // 이벤트 수정 PUT /api/v1/events/{eventId}
    @Transactional
    public Event updateEvent(Long eventId, String title, EventStatus status, LocalDateTime startAt, LocalDateTime endAt) {
        Event event = eventRepository.findByIdAndNotDeleted(eventId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이벤트 입니다. id=" + eventId));

        event.update(title, status, startAt, endAt);
        return event;
    }

    // 이벤트 삭제 (Soft Delete) DELETE /api/v1/events/{eventId}
    @Transactional
    public void deleteEvent(Long eventId) {
        Event event = eventRepository.findByIdAndNotDeleted(eventId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이벤트 입니다. id=" + eventId));

        event.delete();
    }

}
