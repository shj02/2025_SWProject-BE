// src/main/java/com/mongletrip/mongletrip_backend/trip/PersonalBudgetRepository.java

package com.mongletrip.mongletrip_backend.trip;

import com.mongletrip.mongletrip_backend.domain.trip.PersonalBudget;
import com.mongletrip.mongletrip_backend.domain.trip.Trip;
import com.mongletrip.mongletrip_backend.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface PersonalBudgetRepository extends JpaRepository<PersonalBudget, Long> {

    // API 17: 특정 여행의 모든 멤버의 개인 예산 목록 조회
    List<PersonalBudget> findByTrip(Trip trip);

    // API 17: 특정 여행의 전체 예산 총합 계산
    @Query("SELECT SUM(pb.budgetAmount) FROM PersonalBudget pb WHERE pb.trip = :trip")
    Long calculateTotalBudgetAmountByTrip(Trip trip);

    // API 18: 특정 사용자의 개인 예산 조회/갱신용
    Optional<PersonalBudget> findByTripAndUser(Trip trip, User user);

    // 여행 방 삭제 시 연쇄 삭제용
    void deleteByTrip(Trip trip);
}