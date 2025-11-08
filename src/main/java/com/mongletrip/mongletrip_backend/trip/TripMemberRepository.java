// src/main/java/com/mongletrip/mongletrip_backend/trip/TripMemberRepository.java

package com.mongletrip.mongletrip_backend.trip;

import com.mongletrip.mongletrip_backend.domain.trip.Trip;
import com.mongletrip.mongletrip_backend.domain.trip.TripMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying; // 🚨 이 import 문을 추가해야 합니다!
import org.springframework.transaction.annotation.Transactional; // 🚨 이 import 문도 필요합니다!

import java.util.List;
import java.util.Optional;

public interface TripMemberRepository extends JpaRepository<TripMember, Long> {

    // 4. 메인메뉴 - 사용자 ID로 참여하고 있는 모든 여행 목록 찾기
    List<TripMember> findByUserId(Long userId);

    // 5. 방 개설 - 특정 여행에 사용자가 이미 참여했는지 확인
    Optional<TripMember> findByTripAndUserId(Trip trip, Long userId);

    // 4. 메인메뉴 - 특정 여행에 참여하고 있는 총 멤버 수 계산
    int countByTrip(Trip trip);

    /**
     * API 6: 특정 Trip에 연결된 모든 멤버를 삭제하는 메서드
     * @Modifying 어노테이션을 추가하여 데이터베이스 변경을 알립니다.
     */
    @Modifying // 🚨 데이터 변경(DELETE)을 알립니다.
    @Transactional // 트랜잭션 내에서 실행되도록 합니다.
    void deleteByTrip(Trip trip);
}