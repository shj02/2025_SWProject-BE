// src/main/java/com/mongletrip/mongletrip_backend/trip/dto/BudgetStatusResponse.java

package com.mongletrip.mongletrip_backend.trip.dto;

import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@Builder
public class BudgetStatusResponse {

    // --- 전체 예산 현황 (Page 42) ---
    private Long totalBudget;        // 전체 설정 예산
    private Long totalUsed;          // 전체 사용 예산
    private Long totalRemaining;     // 남은 예산
    private Double usagePercentage;  // 사용률 (%)

    // --- 지출 기록 목록 ---
    private List<ExpenseDetail> expenseRecords;

    // --- 개인별 예산 현황 ---
    private List<PersonalBudgetDetail> personalBudgets;

    // --- 정산 대기 현황 (공용 지출) ---
    private Long unsettledSharedAmount; // 미정산 공용 지출 총액
    private int unsettledCount;         // 미정산 공용 지출 건수
    private Long perPersonShare;        // 1인당 정산 금액

    // 지출 상세 DTO
    @Getter
    @Builder
    public static class ExpenseDetail {
        private Long expenseId;
        private String content;
        private Long amount;
        private String type;         // SHARED/PERSONAL
        private String memo;
        private String payerName;    // 결제자 이름 (홍길동님 결제)
        private boolean isSettled;
    }

    // 개인별 예산 상세 DTO
    @Getter
    @Builder
    public static class PersonalBudgetDetail {
        private Long userId;
        private String userName;
        private Long budgetAmount;   // 개인이 설정한 예산
        private Long usedAmount;     // (구현 복잡도상 사용된 금액은 추후 계산 필요)
        private Double usagePercentage; // 사용률 (%)
    }
}