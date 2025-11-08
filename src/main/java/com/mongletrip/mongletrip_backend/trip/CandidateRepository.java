// src/main/java/com/mongletrip/mongletrip_backend/trip/CandidateRepository.java

package com.mongletrip.mongletrip_backend.trip;

import com.mongletrip.mongletrip_backend.domain.trip.Candidate;
import com.mongletrip.mongletrip_backend.domain.trip.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {

    // API 9: 특정 여행의 모든 후보지를 조회
    List<Candidate> findByTrip(Trip trip);

    // API 12: 여행이 삭제될 때 연쇄 삭제용
    void deleteByTrip(Trip trip);
}