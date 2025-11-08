// src/main/java/com/mongletrip/mongletrip_backend/trip/ExpenseRepository.java

package com.mongletrip.mongletrip_backend.trip;

import com.mongletrip.mongletrip_backend.domain.trip.Expense;
import com.mongletrip.mongletrip_backend.domain.trip.Trip;
import com.mongletrip.mongletrip_backend.domain.trip.Expense.ExpenseType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    // API 17: 특정 여행의 모든 지출 내역 조회 (정산 현황 표시용)
    List<Expense> findByTripOrderByExpenseDateDesc(Trip trip);

    // API 17: 특정 여행의 전체 사용된 예산 총합 계산
    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.trip = :trip")
    Long calculateTotalUsedAmountByTrip(Trip trip);

    // API 19: 정산 계산을 위해 정산되지 않은 공용 지출 목록 조회
    List<Expense> findByTripAndTypeAndIsSettledFalse(Trip trip, ExpenseType type);

    // 여행 방 삭제 시 연쇄 삭제용
    void deleteByTrip(Trip trip);
}