// src/main/java/com/mongletrip/mongletrip_backend/trip/dto/TripListResponse.java

package com.mongletrip.mongletrip_backend.trip.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TripListResponse {
    private Long tripId;
    private String name;
    private String destination;
    private String dateRange; // "날짜 미정" 또는 "9/11 ~ 9/12"
    private int memberCount;  // 참여 인원 수
    private int progress;     // 진행률 (0~100)
    private String inviteCode; // 초대 코드
}