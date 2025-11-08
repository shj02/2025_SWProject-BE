// src/main/java/com/mongletrip/mongletrip_backend/trip/dto/ItineraryItemRequest.java

package com.mongletrip.mongletrip_backend.trip.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class ItineraryItemRequest {
    private String title;             // 일정명
    private String placeName;         // 장소명
    private LocalDate scheduleDate;   // 일정 날짜
    private LocalTime startTime;      // 시작 시간
    private Integer estimatedDuration; // 예상 소요시간 (분)
    private String memo;              // 메모 (선택사항)
    private Integer orderIndex;       // Day별 순서 (수정 시 유효)
}