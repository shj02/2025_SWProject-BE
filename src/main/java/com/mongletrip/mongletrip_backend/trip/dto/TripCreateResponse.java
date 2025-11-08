// src/main/java/com/mongletrip/mongletrip_backend/trip/dto/TripCreateResponse.java

package com.mongletrip.mongletrip_backend.trip.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TripCreateResponse {
    private Long tripId;
    private String inviteCode;
    private String message;
}