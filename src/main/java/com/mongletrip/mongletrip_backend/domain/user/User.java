// src/main/java/com/mongletrip/mongletrip_backend/domain/user/User.java

package com.mongletrip.mongletrip_backend.domain.user;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 소셜 로그인 정보 (필수)
    private String socialId; // 네이버/카카오에서 제공하는 고유 ID
    private String provider; // NAVER, KAKAO

    // 기본 정보 (필수)
    @Column(unique = true)
    private String email;

    // 추가 프로필 정보 (Page 12)
    private String name;
    private String phoneNumber;
    private String gender; // MALE, FEMALE
    private LocalDate birthdate;
    private String nationality;

    // 여행 스타일 정보 (Page 13)
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "user_travel_style", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "style")
    private List<String> travelStyles;

    // 가입 상태 관리
    @Builder.Default
    private boolean isRegistered = false; // 필수 프로필 등록 및 스타일 선택 완료 여부

    // 타임스탬프
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // --- 비즈니스 로직 메서드 ---

    // 1. 초기 프로필 업데이트 및 마이페이지 프로필 업데이트 (API 33)
    public void updateProfile(String name, String phoneNumber, String gender, LocalDate birthdate, String nationality) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.birthdate = birthdate;
        this.nationality = nationality;
        // isRegistered는 completeRegistration에서만 true로 변경
    }

    // 2. 여행 스타일 선택 완료 (API 34와 유사하지만 isRegistered 상태를 변경함)
    public void completeRegistration(List<String> travelStyles) {
        this.travelStyles = travelStyles;
        this.isRegistered = true;
    }

    // 3. 마이페이지 여행 스타일 수정 (API 34)
    public void setTravelStyles(List<String> travelStyles) {
        this.travelStyles = travelStyles;
    }
}