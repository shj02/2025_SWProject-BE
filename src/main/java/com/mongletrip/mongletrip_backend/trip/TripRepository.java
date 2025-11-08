// src/main/java/com/mongletrip/mongletrip_backend/trip/TripRepository.java

package com.mongletrip.mongletrip_backend.trip;

import com.mongletrip.mongletrip_backend.domain.trip.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TripRepository extends JpaRepository<Trip, Long> {

    // 5. 방 개설 - 초대 코드로 여행 찾기
    Optional<Trip> findByInviteCode(String inviteCode);
}