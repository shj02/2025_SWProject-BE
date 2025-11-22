package com.mongletrip.mongletrip_backend.trip;

import com.mongletrip.mongletrip_backend.domain.trip.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TripRepository extends JpaRepository<Trip, Long> {

    Optional<Trip> findByInviteCode(String inviteCode);

    // ✔ 유저가 만든 여행 목록 조회 (FE 요구 사항)
    List<Trip> findByCreatorId(Long creatorId);
}
