// src/main/java/com/mongletrip/mongletrip_backend/trip/ItineraryItemRepository.java

package com.mongletrip.mongletrip_backend.trip;

import com.mongletrip.mongletrip_backend.domain.trip.ItineraryItem;
import com.mongletrip.mongletrip_backend.domain.trip.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ItineraryItemRepository extends JpaRepository<ItineraryItem, Long> {

    // API 12: 특정 여행의 전체 일정을 조회 (Day별, 시간순 정렬)
    List<ItineraryItem> findByTripOrderByScheduleDateAscStartTimeAsc(Trip trip);

    // API 14: 여행 방 삭제 시 연쇄 삭제용
    void deleteByTrip(Trip trip);
}