// src/main/java/com/mongletrip/mongletrip_backend/trip/dto/TripCreateRequest.java

package com.mongletrip.mongletrip_backend.trip.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TripCreateRequest {
    private String name;        // 여행 이름 (예: 제주도 우정여행)
    private String destination; // 여행지 (예: 제주도)
}