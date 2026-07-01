package org.coupon.couponsunbbang.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.coupon.couponsunbbang.domain.user.dto.SignupRequest;
import org.coupon.couponsunbbang.domain.user.dto.SignupResponse;
import org.coupon.couponsunbbang.domain.user.entity.User;
import org.coupon.couponsunbbang.domain.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository; // DB에 사용자를 저장하거나 이메일 중복을 확인하기 위해 사용
    private final PasswordEncoder passwordEncoder; // 비밀번호를 암호화하기 위해 사용

    // 회원가입 메서드
    public SignupResponse signup(SignupRequest request) {
        if (userRepository.existsByEmailAndDeletedAtIsNull(request.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        // 사용자가 입력한 원문 비밀번호를 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // DB에 저장할 User 엔티티
        User user = new User(
                request.getName(),
                request.getPhone(),
                request.getEmail(),
                encodedPassword
        );

        User savedUser = userRepository.save(user);

        return SignupResponse.from(savedUser);
    }
}
