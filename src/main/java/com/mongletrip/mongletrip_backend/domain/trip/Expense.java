// src/main/java/com/mongletrip/mongletrip_backend/domain/trip/Expense.java

package com.mongletrip.mongletrip_backend.domain.trip;

import jakarta.persistence.*;
import lombok.*;
import com.mongletrip.mongletrip_backend.domain.user.User;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor // 🚨 모든 필드를 포함하는 생성자 (단 한 번!)
@Builder(toBuilder = true) // 🚨 Builder와 toBuilder 기능을 함께 활성화 (단 한 번!)
@Table(name = "expense")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id")
    private Trip trip;

    private String content;     // 지출 내용 (예: 숙소 예약)
    private Long amount;        // 지출 금액 (Long 타입 사용)

    // 지출 유형 (공용: SHARED, 개인: PERSONAL)
    @Enumerated(EnumType.STRING)
    private ExpenseType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payer_id")
    private User payer;         // 결제자

    private String memo;        // 메모 (어디어디리조트 등)

    private LocalDateTime expenseDate; // 지출 기록 시간

    // 이 지출이 정산이 완료되었는지 여부
    private boolean isSettled = false;

    // ExpenseType Enum 정의
    public enum ExpenseType {
        SHARED, // 공용 지출
        PERSONAL // 개인 지출
    }
}