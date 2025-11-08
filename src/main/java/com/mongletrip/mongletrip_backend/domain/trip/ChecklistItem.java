// src/main/java/com/mongletrip/mongletrip_backend/domain/trip/ChecklistItem.java

package com.mongletrip.mongletrip_backend.domain.trip;

import jakarta.persistence.*;
import lombok.*;
import com.mongletrip.mongletrip_backend.domain.user.User;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true) // 수정 기능을 위해 toBuilder 사용
@Table(name = "checklist_item")
public class ChecklistItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id")
    private Trip trip;

    // 체크리스트 유형 (공용: SHARED, 개인: PERSONAL)
    @Enumerated(EnumType.STRING)
    private ChecklistType type;

    private String content;     // 할 일 내용 (예: 항공권 예약)
    private String description; // 자세한 설명 (선택)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_user_id")
    private User assignedUser;  // 담당자 (공용 체크리스트일 경우)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_user_id")
    private User ownerUser;     // 소유자 (개인 체크리스트일 경우)

    private LocalDate dueDate; // 마감일 (선택)

    private boolean isCompleted = false; // 완료 여부

    public enum ChecklistType {
        SHARED, // 공용 체크리스트
        PERSONAL // 개인 체크리스트
    }

    // 완료 상태 토글 (수정 기능)
    public void toggleCompletion() {
        this.isCompleted = !this.isCompleted;
    }
}