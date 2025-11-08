// src/main/java/com/mongletrip/mongletrip_backend/trip/CandidateVoteRepository.java

package com.mongletrip.mongletrip_backend.trip;

import com.mongletrip.mongletrip_backend.domain.trip.Candidate;
import com.mongletrip.mongletrip_backend.domain.trip.CandidateVote;
import com.mongletrip.mongletrip_backend.domain.trip.Trip;
import com.mongletrip.mongletrip_backend.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

public interface CandidateVoteRepository extends JpaRepository<CandidateVote, Long> {

    // API 11: 특정 사용자가 특정 후보지에 투표했는지 확인
    Optional<CandidateVote> findByCandidateAndUser(Candidate candidate, User user);

    // API 9: 특정 여행에서 사용자가 투표한 모든 후보지 목록을 조회 (투표 여부 표시용)
    List<CandidateVote> findByUserAndCandidate_Trip(User user, Trip trip);

    // API 12: 후보지가 삭제될 때 연쇄 삭제용
    @Modifying
    @Transactional
    void deleteByCandidate(Candidate candidate);
}