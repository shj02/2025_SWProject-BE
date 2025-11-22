package com.mongletrip.mongletrip_backend.user;

import com.mongletrip.mongletrip_backend.common.util.SecurityUtil;
import com.mongletrip.mongletrip_backend.user.dto.ProfileUpdateRequest;
import com.mongletrip.mongletrip_backend.user.dto.StyleUpdateRequest;
import com.mongletrip.mongletrip_backend.user.dto.UserDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users/me")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * API 1: 초기 프로필 정보 입력
     * (POST /api/users/me/profile/initial)
     */
    @PostMapping("/profile/initial")
    public ResponseEntity<Map<String, Object>> completeProfile(@RequestBody ProfileUpdateRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();

        userService.updateProfileInitial(
                userId,
                request.getName(),
                request.getPhoneNumber(),
                request.getGender(),
                request.getBirthdate(),
                request.getNationality()
        );

        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "message", "초기 프로필 정보 저장 완료"
                )
        );
    }

    /**
     * API 2: 여행 스타일 선택 및 회원가입 최종 완료
     * (PUT /api/users/me/styles/complete)
     */
    @PutMapping("/styles/complete")
    public ResponseEntity<Map<String, Object>> selectStyles(@RequestBody StyleUpdateRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();
        userService.completeRegistration(userId, request.getTravelStyles());

        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "message", "여행 스타일 저장 및 회원가입 완료"
                )
        );
    }

    /**
     * API 32: 프로필 조회
     * (GET /api/users/me)
     */
    @GetMapping
    public ResponseEntity<UserDetailResponse> getUserDetail() {
        Long userId = SecurityUtil.getCurrentUserId();
        UserDetailResponse response = userService.getUserDetail(userId);
        return ResponseEntity.ok(response);
    }

    /**
     * API 33: 프로필 계정 정보 수정
     * (PUT /api/users/me/profile)
     */
    @PutMapping("/profile")
    public ResponseEntity<Map<String, Object>> updateProfile(@RequestBody ProfileUpdateRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();
        userService.updateProfile(userId, request);

        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "message", "프로필 정보 수정 완료"
                )
        );
    }

    /**
     * API 34: 여행 스타일 수정
     * (PUT /api/users/me/styles)
     */
    @PutMapping("/styles")
    public ResponseEntity<Map<String, Object>> updateTravelStyles(@RequestBody StyleUpdateRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();
        userService.updateTravelStyles(userId, request);

        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "message", "여행 스타일 수정 완료"
                )
        );
    }
}
