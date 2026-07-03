package org.coupon.couponsunbbang.domain.event.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.coupon.couponsunbbang.domain.event.entity.EventStatus;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class UpdateEventRequest {

    @NotBlank
    private String title;

    @NotBlank
    private EventStatus status;

    @NotBlank
    private LocalDateTime startAt;

    @NotBlank
    private LocalDateTime endAt;
}
