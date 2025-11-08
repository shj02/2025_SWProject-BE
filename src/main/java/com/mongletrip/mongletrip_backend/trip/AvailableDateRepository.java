// src/main/java/com/mongletrip/mongletrip_backend/trip/AvailableDateRepository.java

package com.mongletrip.mongletrip_backend.trip;

import com.mongletrip.mongletrip_backend.domain.trip.AvailableDate;
import com.mongletrip.mongletrip_backend.domain.trip.AvailableDateId;
import com.mongletrip.mongletrip_backend.domain.trip.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.time.LocalDate;

public interface AvailableDateRepository extends JpaRepository<AvailableDate, AvailableDateId> {

    // API 6: 특정 사용자가 이전에 제출한 모든 가능 날짜를 삭제 (갱신을 위해 필요)
    @Modifying
    @Transactional
    void deleteByTripAndUserId(Trip trip, Long userId);

    // API 7: 특정 여행의 모든 가능 날짜 목록을 조회 (합의 현황 계산용)
    List<AvailableDate> findByTrip(Trip trip);

    // API 8: 특정 날짜에 가능한 사용자 수 조회 (날짜 확정 시 유효성 검사용)
    List<AvailableDate> findByTripAndPossibleDate(Trip trip, LocalDate possibleDate);
}