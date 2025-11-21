// src/main/java/com/mongletrip/mongletrip_backend/user/UserService.java

package com.mongletrip.mongletrip_backend.user;

import com.mongletrip.mongletrip_backend.common.exception.ResourceNotFoundException;
import com.mongletrip.mongletrip_backend.domain.user.User;
import com.mongletrip.mongletrip_backend.user.dto.ProfileUpdateRequest;
import com.mongletrip.mongletrip_backend.user.dto.StyleUpdateRequest;
import com.mongletrip.mongletrip_backend.user.dto.UserDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.time.LocalDate; // updateProfile 메서드 등에서 사용됨

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // --- 1. 가입 시 초기 프로필 등록 (Page 12) ---
    // (초기 가입 시 필요한 DTO가 별도로 있다고 가정하고, updateProfileInitial로 명명)
    @Transactional
    public void updateProfileInitial(Long userId, String name, String phoneNumber, String gender, LocalDate birthdate, String nationality) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다."));

        // User 엔티티의 updateProfile 메서드 (이전에 정의됨) 사용
        user.updateProfile(name, phoneNumber, gender, birthdate, nationality);

        // userRepository.save(user); // @Transactional로 자동 반영
    }

    // --- 2. 가입 시 여행 스타일 선택 및 최종 완료 (Page 13) ---
    @Transactional
    public void completeRegistration(Long userId, List<String> travelStyles) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다."));

        // User 엔티티의 completeRegistration 메서드 (이전에 정의됨) 사용
        user.completeRegistration(travelStyles);
    }

    // --- 13. 마이페이지 기능 ---

    /**
     * API 32: 프로필 조회 (GET /api/users/me) - Page 66
     */
    @Transactional(readOnly = true)
    public UserDetailResponse getUserDetail(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다."));

        return UserDetailResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .gender(user.getGender())
                .birthdate(user.getBirthdate())
                .nationality(user.getNationality())
                .travelStyles(user.getTravelStyles())
                .build();
    }

    /**
     * API 33: 프로필 계정 정보 수정 (PUT /api/users/me/profile) - Page 66
     */
    @Transactional
    public void updateProfile(Long userId, ProfileUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다."));

        // User 엔티티의 updateProfile 메서드를 재활용하여 프로필 정보 업데이트
        user.updateProfile(
                request.getName(),
                request.getPhoneNumber(),
                request.getGender(),
                request.getBirthDate(),
                request.getNationality()
        );
    }

    /**
     * API 34: 여행 스타일 수정 (PUT /api/users/me/styles) - Page 66
     */
    @Transactional
    public void updateTravelStyles(Long userId, StyleUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다."));

        // User 엔티티에 setTravelStyles 메서드가 정의되어 있다고 가정하고 업데이트
        user.setTravelStyles(request.getTravelStyles());
    }
}