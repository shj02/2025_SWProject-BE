// src/main/java/com/mongletrip/mongletrip_backend/trip/dto/ChecklistListResponse.java

package com.mongletrip.mongletrip_backend.trip.dto;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class ChecklistListResponse {

    // 공용 체크리스트 목록 (Page 50)
    private List<ChecklistDetail> sharedChecklists;

    // 개인 체크리스트 목록 (Page 50)
    private List<ChecklistDetail> personalChecklists;

    // 체크리스트 상세 DTO
    @Getter
    @Builder
    public static class ChecklistDetail {
        private Long itemId;
        private String content;
        private String description;
        private String assignedUserName; // 담당자 이름 (공용일 경우)
        private String ownerUserName;    // 소유자 이름 (개인일 경우)
        private LocalDate dueDate;
        private boolean isCompleted;
    }
}