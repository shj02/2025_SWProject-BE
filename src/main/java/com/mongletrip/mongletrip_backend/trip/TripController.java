// src/main/java/com/mongletrip/mongletrip_backend/trip/TripController.java

package com.mongletrip.mongletrip_backend.trip;

import com.mongletrip.mongletrip_backend.common.util.SecurityUtil;
import com.mongletrip.mongletrip_backend.trip.dto.TripCreateRequest;
import com.mongletrip.mongletrip_backend.trip.dto.TripCreateResponse;
import com.mongletrip.mongletrip_backend.trip.dto.TripListResponse;
import com.mongletrip.mongletrip_backend.trip.dto.AvailableDateRequest; // 7. 날짜 기능 DTO
import com.mongletrip.mongletrip_backend.trip.dto.DateConfirmRequest;  // 7. 날짜 기능 DTO
import com.mongletrip.mongletrip_backend.trip.dto.DateStatusResponse;   // 7. 날짜 기능 DTO
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.mongletrip.mongletrip_backend.trip.dto.ItineraryItemRequest;
import com.mongletrip.mongletrip_backend.trip.dto.ItineraryListResponse;
import com.mongletrip.mongletrip_backend.trip.dto.ChecklistRequest;
import com.mongletrip.mongletrip_backend.trip.dto.ChecklistListResponse;

@RestController
@RequestMapping("/api/trips")
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;

    // --- 5. 방 개설 기능 ---

    /**
     * API 5: 여행 방 생성 (POST /api/trips) - Page 16
     */
    @PostMapping
    public ResponseEntity<TripCreateResponse> createTrip(@RequestBody TripCreateRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();
        TripCreateResponse response = tripService.createTrip(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * API 5: 초대 코드로 여행 참여 (POST /api/trips/join/{inviteCode}) - Page 16
     */
    @PostMapping("/join/{inviteCode}")
    public ResponseEntity<String> joinTrip(@PathVariable String inviteCode) {
        Long userId = SecurityUtil.getCurrentUserId();
        tripService.joinTripByInviteCode(userId, inviteCode);
        return ResponseEntity.ok("여행에 성공적으로 참여했습니다.");
    }

    // --- 4. 메인메뉴 & 6. 사이드바 기능 ---

    /**
     * API 4: 사용자의 여행 목록 조회 (GET /api/users/me/trips) - Page 14, 18
     */
    @GetMapping("/api/users/me/trips")
    public ResponseEntity<List<TripListResponse>> getMyTrips() {
        Long userId = SecurityUtil.getCurrentUserId();
        List<TripListResponse> trips = tripService.getTripsByUserId(userId);
        return ResponseEntity.ok(trips);
    }

    /**
     * API 6: 여행 방 삭제 (DELETE /api/trips/{tripId}) - Page 19
     * 권한: 방장만 가능
     */
    @DeleteMapping("/{tripId}")
    public ResponseEntity<Void> deleteTrip(@PathVariable Long tripId) {
        Long userId = SecurityUtil.getCurrentUserId();
        tripService.deleteTrip(userId, tripId);
        return ResponseEntity.noContent().build();
    }

    // --- 7. 날짜 합의 기능 ---

    /**
     * API 6: 내 가능 날짜 등록/수정 (PUT /api/trips/{tripId}/available-dates) - Page 28
     */
    @PutMapping("/{tripId}/available-dates")
    public ResponseEntity<String> updateAvailableDates(@PathVariable Long tripId,
                                                       @RequestBody AvailableDateRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();
        tripService.updateAvailableDates(userId, tripId, request);
        return ResponseEntity.ok("가능 날짜가 성공적으로 업데이트되었습니다.");
    }

    /**
     * API 7: 날짜 합의 현황 조회 (GET /api/trips/{tripId}/date-status) - Page 27
     */
    @GetMapping("/{tripId}/date-status")
    public ResponseEntity<DateStatusResponse> getDateStatus(@PathVariable Long tripId) {
        DateStatusResponse response = tripService.getDateStatus(tripId);
        return ResponseEntity.ok(response);
    }

    /**
     * API 8: 여행 날짜 확정 (PUT /api/trips/{tripId}/date-confirm) - Page 30
     */
    @PutMapping("/{tripId}/date-confirm")
    public ResponseEntity<String> confirmDate(@PathVariable Long tripId,
                                              @RequestBody DateConfirmRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();
        tripService.confirmDate(userId, tripId, request);
        return ResponseEntity.ok("여행 날짜가 확정되었습니다: " + request.getStartDate() + " ~ " + request.getEndDate());
    }

    /**
     * API 12: 일정표 전체 조회 (GET /api/trips/{tripId}/itinerary) - Page 35
     */
    @GetMapping("/{tripId}/itinerary")
    public ResponseEntity<ItineraryListResponse> getItinerary(@PathVariable Long tripId) {
        ItineraryListResponse response = tripService.getItinerary(tripId);
        return ResponseEntity.ok(response);
    }

    /**
     * API 13: 새 일정 추가 (POST /api/trips/{tripId}/itinerary) - Page 39
     */
    @PostMapping("/{tripId}/itinerary")
    public ResponseEntity<Long> createItineraryItem(@PathVariable Long tripId,
                                                    @RequestBody ItineraryItemRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();
        Long itemId = tripService.createItineraryItem(userId, tripId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(itemId);
    }

    /**
     * API 14: 일정 수정 (PUT /api/itinerary/{itemId}) - Page 41
     */
    @PutMapping("/itinerary/{itemId}")
    public ResponseEntity<Void> updateItineraryItem(@PathVariable Long itemId,
                                                    @RequestBody ItineraryItemRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();
        tripService.updateItineraryItem(userId, itemId, request);
        return ResponseEntity.ok().build();
    }

    /**
     * API 15: 일정 삭제 (DELETE /api/itinerary/{itemId}) - Page 37
     */
    @DeleteMapping("/itinerary/{itemId}")
    public ResponseEntity<Void> deleteItineraryItem(@PathVariable Long itemId) {
        tripService.deleteItineraryItem(itemId);
        return ResponseEntity.noContent().build();
    }

    // --- 11. 체크리스트 기능 ---

    /**
     * API 20: 체크리스트 목록 조회 (GET /api/trips/{tripId}/checklists) - Page 50
     */
    @GetMapping("/{tripId}/checklists")
    public ResponseEntity<ChecklistListResponse> getChecklists(@PathVariable Long tripId) {
        Long userId = SecurityUtil.getCurrentUserId();
        ChecklistListResponse response = tripService.getChecklists(userId, tripId);
        return ResponseEntity.ok(response);
    }

    /**
     * API 21: 체크리스트 항목 추가 (POST /api/trips/{tripId}/checklists) - Page 52, 54
     */
    @PostMapping("/{tripId}/checklists")
    public ResponseEntity<Long> createChecklistItem(@PathVariable Long tripId,
                                                    @RequestBody ChecklistRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();
        Long itemId = tripService.createChecklistItem(userId, tripId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(itemId);
    }

    /**
     * API 22: 체크리스트 완료 상태 토글 (PUT /api/checklists/{itemId}/toggle) - Page 50, 58
     * 참고: PUT 요청이지만, 토글은 /api/checklists로 경로를 분리합니다.
     */
    @PutMapping("/checklists/{itemId}/toggle")
    public ResponseEntity<Void> toggleChecklistCompletion(@PathVariable Long itemId) {
        tripService.toggleChecklistCompletion(itemId);
        return ResponseEntity.ok().build();
    }

    /**
     * API 23: 체크리스트 항목 삭제 (DELETE /api/checklists/{itemId})
     */
    @DeleteMapping("/checklists/{itemId}")
    public ResponseEntity<Void> deleteChecklistItem(@PathVariable Long itemId) {
        // 실제로는 삭제 권한(소유자, 담당자, 방장) 확인 로직이 필요함.
        tripService.deleteChecklistItem(itemId);
        return ResponseEntity.noContent().build();
    }
}