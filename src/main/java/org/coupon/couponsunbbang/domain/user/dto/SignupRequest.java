package org.coupon.couponsunbbang.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignupRequest {
    private String name;
    private String phone;
    private String email;
    private String password;
}

