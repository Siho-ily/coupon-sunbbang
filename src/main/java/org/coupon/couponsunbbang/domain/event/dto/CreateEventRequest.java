package org.coupon.couponsunbbang.domain.event.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.coupon.couponsunbbang.domain.event.entity.EventStatus;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CreateEventRequest {

    @NotBlank
    private String title;

    @NotNull
    private EventStatus status;

    @NotNull
    private LocalDateTime startAt;

    @NotNull
    private LocalDateTime endAt;
}
