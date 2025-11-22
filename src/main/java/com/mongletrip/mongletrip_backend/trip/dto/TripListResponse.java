package com.mongletrip.mongletrip_backend.trip.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TripListResponse {

    private Long id;            // tripId → id
    private String title;       // name → title
    private String startDate;   // dateRange에서 분리
    private String endDate;     // dateRange에서 분리
    private int participants;   // memberCount → participants
    private String inviteCode;
    private String status;      // 진행 상태 ("PLANNING" 등)
}
