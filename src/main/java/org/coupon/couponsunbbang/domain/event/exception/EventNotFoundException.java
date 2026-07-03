package org.coupon.couponsunbbang.domain.event.exception;

import org.coupon.couponsunbbang.global.exception.BusinessException;
import org.coupon.couponsunbbang.global.exception.ErrorCode;

public class EventNotFoundException extends BusinessException {

    public EventNotFoundException() {
        super(ErrorCode.EVENT_NOT_FOUND);
    }
}