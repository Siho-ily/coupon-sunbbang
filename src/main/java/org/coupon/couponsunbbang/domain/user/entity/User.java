package org.coupon.couponsunbbang.domain.user.entity;

import org.coupon.couponsunbbang.global.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 회원 고유번호

    @Column(nullable = false, length = 100)
    private String name; // 이름

    @Column(length = 20)
    private String phone; // 휴대전화

    @Column(nullable = false, unique = true, length = 255)
    private String email; // 이메일

    @Column(nullable = false, length = 255)
    private String password; // 비밀번호 -> 암호화는 UserService에서 함

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private UserRole role; // 회원 권한

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt; // 회원탈퇴 시간

    // 회원가입 시 사용할 생성자
    public User(String name, String phone, String email, String password) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.role = UserRole.USER;
    }

    // 프로필 수정
    public void updateProfile(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    // 회원탈퇴 처리
    public void delete() {
        this.deletedAt = LocalDateTime.now();
        // DB row를 삭제하지 않고, 현재 시간을 넣음
    }
}
