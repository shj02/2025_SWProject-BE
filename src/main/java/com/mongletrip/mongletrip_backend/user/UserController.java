// src/main/java/com/mongletrip/mongletrip_backend/user/UserController.java

package com.mongletrip.mongletrip_backend.user;

import com.mongletrip.mongletrip_backend.common.util.SecurityUtil;
import com.mongletrip.mongletrip_backend.user.dto.ProfileUpdateRequest;
import com.mongletrip.mongletrip_backend.user.dto.StyleUpdateRequest;
import com.mongletrip.mongletrip_backend.user.dto.UserDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/me") // 마이페이지 관련 경로는 /api/users/me로 통일
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // --- 가입 후 추가 정보 입력 단계 (Page 12, 13) ---

    /**
     * API 1: 초기 프로필 정보 입력 (POST /api/users/me/profile/initial) - Page 12
     */
    @PostMapping("/profile/initial")
    public ResponseEntity<Void> completeProfile(@RequestBody ProfileUpdateRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();
        // UserService의 초기 업데이트 메서드를 호출
        userService.updateProfileInitial(
                userId,
                request.getName(),
                request.getPhoneNumber(),
                request.getGender(),
                request.getBirthdate(),
                request.getNationality()
        );
        return ResponseEntity.ok().build();
    }

    /**
     * API 2: 여행 스타일 선택 및 회원가입 최종 완료 (PUT /api/users/me/styles/complete) - Page 13
     */
    @PutMapping("/styles/complete")
    public ResponseEntity<Void> selectStyles(@RequestBody StyleUpdateRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();
        userService.completeRegistration(userId, request.getTravelStyles());
        return ResponseEntity.ok().build();
    }

    // --- 마이페이지 프로필 관리 기능 (Page 66) ---

    /**
     * API 32: 프로필 조회 (GET /api/users/me)
     */
    @GetMapping
    public ResponseEntity<UserDetailResponse> getUserDetail() {
        Long userId = SecurityUtil.getCurrentUserId();
        UserDetailResponse response = userService.getUserDetail(userId);
        return ResponseEntity.ok(response);
    }

    /**
     * API 33: 프로필 계정 정보 수정 (PUT /api/users/me/profile)
     */
    @PutMapping("/profile")
    public ResponseEntity<Void> updateProfile(@RequestBody ProfileUpdateRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();
        userService.updateProfile(userId, request);
        return ResponseEntity.ok().build();
    }

    /**
     * API 34: 여행 스타일 수정 (PUT /api/users/me/styles)
     */
    @PutMapping("/styles")
    public ResponseEntity<Void> updateTravelStyles(@RequestBody StyleUpdateRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();
        userService.updateTravelStyles(userId, request);
        return ResponseEntity.ok().build();
    }
}