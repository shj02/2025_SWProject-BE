// src/main/java/com/mongletrip/mongletrip_backend/trip/dto/ExpenseRequest.java

package com.mongletrip.mongletrip_backend.trip.dto;

import com.mongletrip.mongletrip_backend.domain.trip.Expense.ExpenseType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ExpenseRequest {
    private String content;     // 지출 내용
    private Long amount;        // 지출 금액
    private String memo;        // 메모 (어디어디리조트)
    private ExpenseType type;   // SHARED(공용) 또는 PERSONAL(개인)
    private Long payerId;       // 결제자 ID (Long)
}