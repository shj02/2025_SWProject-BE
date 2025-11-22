package com.mongletrip.mongletrip_backend.trip;

import com.mongletrip.mongletrip_backend.domain.trip.Trip;
import com.mongletrip.mongletrip_backend.common.util.SecurityUtil;
import com.mongletrip.mongletrip_backend.trip.dto.TripCreateRequest;
import com.mongletrip.mongletrip_backend.trip.dto.TripCreateResponse;
import com.mongletrip.mongletrip_backend.trip.dto.TripListResponse;
import com.mongletrip.mongletrip_backend.trip.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TripService {

    private final TripRepository tripRepository;

    // =============================
    //   여행 생성
    // =============================
    public TripCreateResponse createTrip(TripCreateRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();

        Trip trip = Trip.builder()
                .name(request.getName())
                .destination(request.getDestination())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .inviteCode(generateInviteCode())
                .creatorId(userId)
                .build();

        Trip saved = tripRepository.save(trip);

        return TripCreateResponse.builder()
                .tripId(saved.getId())
                .inviteCode(saved.getInviteCode())
                .message("여행 생성 완료")
                .build();
    }

    // =============================
    //   내 여행 목록 조회
    // =============================
    public List<TripListResponse> getTripsByUserId() {
        Long userId = SecurityUtil.getCurrentUserId();
        List<Trip> trips = tripRepository.findByCreatorId(userId);

        return trips.stream().map(trip -> TripListResponse.builder()
                .id(trip.getId())
                .title(trip.getName())
                .startDate(trip.getStartDate() != null ? trip.getStartDate().toString() : "")
                .endDate(trip.getEndDate() != null ? trip.getEndDate().toString() : "")
                .participants(1) // 현재는 creator 1명만
                .inviteCode(trip.getInviteCode())
                .status("PLANNING")
                .build()).toList();
    }

    // =============================
    //   여행 삭제
    // =============================
    public void deleteTrip(Long tripId) {
        tripRepository.deleteById(tripId);
    }


    // =============================
    //   초대코드 생성 유틸
    // =============================
    private String generateInviteCode() {
        return Long.toHexString(Double.doubleToLongBits(Math.random())).substring(0, 8).toUpperCase();
    }
}
