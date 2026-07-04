package org.coupon.couponsunbbang.domain.user.exception;

import org.coupon.couponsunbbang.global.exception.BusinessException;
import org.coupon.couponsunbbang.global.exception.ErrorCode;

public class DuplicateEmailException extends BusinessException {

    public DuplicateEmailException() {
        super(ErrorCode.DUPLICATE_RESOURCE, "이미 사용 중인 이메일입니다.");
    }
}
