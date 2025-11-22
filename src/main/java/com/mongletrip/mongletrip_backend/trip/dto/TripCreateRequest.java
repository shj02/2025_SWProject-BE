package com.mongletrip.mongletrip_backend.trip.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;


@Getter
@NoArgsConstructor
public class TripCreateRequest {
    private String name;
    private String destination;
    private LocalDate startDate;
    private LocalDate endDate;
}