// src/main/java/com/mongletrip/mongletrip_backend/trip/dto/ChecklistRequest.java

package com.mongletrip.mongletrip_backend.trip.dto;

import com.mongletrip.mongletrip_backend.domain.trip.ChecklistItem.ChecklistType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class ChecklistRequest {
    private ChecklistType type;     // SHARED(공용) 또는 PERSONAL(개인)
    private String content;         // 할 일 내용
    private String description;     // 자세한 설명 (선택)
    private Long assignedUserId;    // 담당자 ID (공용일 경우)
    private LocalDate dueDate;      // 마감일 (선택)
}