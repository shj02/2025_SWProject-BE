// src/main/java/com/mongletrip/mongletrip_backend/trip/dto/ItineraryListResponse.java

package com.mongletrip.mongletrip_backend.trip.dto;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Getter
@Builder
public class ItineraryListResponse {

    // Day별로 그룹화된 일정 목록
    private Map<LocalDate, List<ItineraryItemDetail>> dailyItineraries;

    // Day별 일정 상세 DTO
    @Getter
    @Builder
    public static class ItineraryItemDetail {
        private Long itemId;
        private String title;
        private String placeName;
        private LocalTime startTime;
        private Integer estimatedDuration;
        private String memo;
        private Long lastEditorId;
        private int orderIndex;
    }
}