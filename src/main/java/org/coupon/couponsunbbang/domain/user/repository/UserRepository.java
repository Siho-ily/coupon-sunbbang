package org.coupon.couponsunbbang.domain.user.repository;

import org.coupon.couponsunbbang.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmailAndDeletedAtIsNull(String email); // 이메일 중복 검사용 메서드
    Optional<User> findByEmailAndDeletedAtIsNull(String email); // 로그인 시 이메일로 사용자 찾는 메서드
    Optional<User> findByIdAndDeletedAtIsNull(Long id); // 마이페이지에서 id로 사용자 찾는 메서드
}
