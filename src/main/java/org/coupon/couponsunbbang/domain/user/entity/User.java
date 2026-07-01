package org.coupon.couponsunbbang.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class User {
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

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt; // 회원 생성 시간

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt; // 회원 정보 수정 시간

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

    // DB 저장 직전에 생성 시간과 수정 시간을 현재 시간으로 넣는 메서드
    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    // DB row가 수정되기 직전에 수정 시간을 현재 시간으로 넣는 메서드
    @PreUpdate
    public void preUpdate() {
        LocalDateTime now = LocalDateTime.now();
        this.updatedAt = now;
    }
}
