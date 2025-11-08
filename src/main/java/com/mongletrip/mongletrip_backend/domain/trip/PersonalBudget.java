// src/main/java/com/mongletrip/mongletrip_backend/domain/trip/PersonalBudget.java

package com.mongletrip.mongletrip_backend.domain.trip;

import jakarta.persistence.*;
import lombok.*;
import com.mongletrip.mongletrip_backend.domain.user.User;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "personal_budget", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"trip_id", "user_id"}) // 멤버별로 한 여행에 하나의 예산만 설정 가능
})
public class PersonalBudget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // 개인이 설정한 예산 금액 (Long 타입 사용)
    private Long budgetAmount;

    // 예산 금액 수정 기능을 위해 Setter를 추가하거나 toBuilder 사용
    public void updateBudget(Long newAmount) {
        this.budgetAmount = newAmount;
    }
}