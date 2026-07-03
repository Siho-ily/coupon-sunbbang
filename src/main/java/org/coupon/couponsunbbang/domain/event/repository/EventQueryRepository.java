package org.coupon.couponsunbbang.domain.event.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.coupon.couponsunbbang.domain.event.entity.Event;
import org.coupon.couponsunbbang.domain.event.entity.EventStatus;
import org.coupon.couponsunbbang.domain.event.entity.QEvent;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class EventQueryRepository {

    private final JPAQueryFactory queryFactory;
    private static final QEvent event = QEvent.event;

    // 단건 조회 (삭제되지 않은 이벤트)
    public Optional<Event> findById(Long id) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(event)
                        .where(event.id.eq(id)
                                .and(event.deletedAt.isNull()))
                        .fetchOne()
        );
    }

    // 전체 목록 조회 (삭제되지 않은 이벤트)
    public List<Event> findAll() {
        return queryFactory
                .selectFrom(event)
                .where(event.deletedAt.isNull())
                .fetch();
    }

    // 상태별 목록 조회
    public List<Event> findByStatus(EventStatus status) {
        return queryFactory
                .selectFrom(event)
                .where(event.status.eq(status)
                        .and(event.deletedAt.isNull()))
                .fetch();
    }
}