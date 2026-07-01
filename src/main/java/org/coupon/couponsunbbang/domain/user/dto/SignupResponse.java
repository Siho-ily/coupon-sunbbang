package org.coupon.couponsunbbang.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.coupon.couponsunbbang.domain.user.entity.User;
import org.coupon.couponsunbbang.domain.user.entity.UserRole;

@Getter
@AllArgsConstructor
public class SignupResponse { // 회원가입 성공 응답을 담는 클래스
    private Long id;
    private String name;
    private String phone;
    private String email;
    private UserRole role;

    public static SignupResponse from(User user) {
        return new SignupResponse(
                user.getId(),
                user.getName(),
                user.getPhone(),
                user.getEmail(),
                user.getRole()
        );
    }
}
