// src/main/java/com/mongletrip/mongletrip_backend/trip/ChecklistItemRepository.java

package com.mongletrip.mongletrip_backend.trip;

import com.mongletrip.mongletrip_backend.domain.trip.ChecklistItem;
import com.mongletrip.mongletrip_backend.domain.trip.Trip;
import com.mongletrip.mongletrip_backend.domain.trip.ChecklistItem.ChecklistType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChecklistItemRepository extends JpaRepository<ChecklistItem, Long> {

    // API 20: 특정 여행의 모든 체크리스트 조회 (정렬: 유형별, 미완료 우선)
    List<ChecklistItem> findByTripOrderByTypeAscIsCompletedAsc(Trip trip);

    // API 20: 특정 여행의 공용 체크리스트만 조회
    List<ChecklistItem> findByTripAndType(Trip trip, ChecklistType type);

    // API 20: 특정 여행의 개인 체크리스트만 조회
    List<ChecklistItem> findByTripAndOwnerUser_IdAndType(Trip trip, Long ownerUserId, ChecklistType type);

    // 여행 방 삭제 시 연쇄 삭제용
    void deleteByTrip(Trip trip);
}