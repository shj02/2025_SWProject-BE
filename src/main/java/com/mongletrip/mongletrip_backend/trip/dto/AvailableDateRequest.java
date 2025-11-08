// src/main/java/com/mongletrip/mongletrip_backend/trip/dto/AvailableDateRequest.java

package com.mongletrip.mongletrip_backend.trip.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class AvailableDateRequest {
    // 사용자가 선택한 가능한 날짜 목록 (Page 28)
    private List<LocalDate> possibleDates;
}