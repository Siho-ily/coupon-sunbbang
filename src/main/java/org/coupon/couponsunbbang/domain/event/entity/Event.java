package org.coupon.couponsunbbang.domain.event.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "event")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EventStatus status;

    @Column(name = "start_at", nullable = false)
    private LocalDateTime startAt;

    @Column(name = "end_at", nullable = false)
    private LocalDateTime endAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Soft Delete 컬럼 null이 아니면 삭제된 것으로 간주
    // Hard Delete 시 쿠폰마스터 / 쿠폰이슈가 참조되는 대상이 연쇄 삭제되어 데이터 무결성 위반
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Builder
    public Event(String title, EventStatus status, LocalDateTime startAt, LocalDateTime endAt) {
        this.title = title;
        this.status = status;
        this.startAt = startAt;
        this.endAt = endAt;
    }

    // 저장 직전 자동 호출 (createdAt, updated 자동 세팅)
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // 수동 직전 자동 호출 (updatedAt만 갱신)
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // 이벤트 수정: PUT /api/v1/enents/{eventId}
    public void update(String title, EventStatus status, LocalDateTime startAt, LocalDateTime endAt) {
        this.title = title;
        this.status = status;
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }

    // 이벤트가 활성 상태인지 체크
    public boolean isActive() {
        return this.status == EventStatus.ACTIVE && this.deletedAt == null;
    }

    // 현재 시각이 이벤트 기간 안에 있는지 체크
    public boolean isWithinPeriod(LocalDateTime now) {
        return !now.isBefore(this.startAt) && !now.isAfter(this.endAt);
    }
}
