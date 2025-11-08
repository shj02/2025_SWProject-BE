// src/main/java/com/mongletrip/mongletrip_backend/trip/dto/DateConfirmRequest.java

package com.mongletrip.mongletrip_backend.trip.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class DateConfirmRequest {
    private LocalDate startDate; // 시작 날짜
    private LocalDate endDate;   // 종료 날짜
}