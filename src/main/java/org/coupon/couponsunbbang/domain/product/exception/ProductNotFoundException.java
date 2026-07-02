package org.coupon.couponsunbbang.domain.product.exception;

import org.coupon.couponsunbbang.global.exception.BusinessException;
import org.coupon.couponsunbbang.global.exception.ErrorCode;

public class ProductNotFoundException extends BusinessException {
	public ProductNotFoundException() {
		super(ErrorCode.PRODUCT_NOT_FOUND);
	}
}
