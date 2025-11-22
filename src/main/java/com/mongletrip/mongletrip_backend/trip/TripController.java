package com.mongletrip.mongletrip_backend.trip;


import com.mongletrip.mongletrip_backend.trip.dto.TripCreateRequest;
import com.mongletrip.mongletrip_backend.trip.dto.TripListResponse;
import com.mongletrip.mongletrip_backend.trip.dto.TripCreateResponse;
import com.mongletrip.mongletrip_backend.trip.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TripController {


    private final TripService tripService;


    @PostMapping("/trips")
    public ResponseEntity<TripCreateResponse> createTrip(@RequestBody TripCreateRequest request) {
        TripCreateResponse response = tripService.createTrip(request);
        return ResponseEntity.status(201).body(response);
    }


    @GetMapping("/users/me/trips")
    public ResponseEntity<List<TripListResponse>> getMyTrips() {
        List<TripListResponse> trips = tripService.getTripsByUserId();
        return ResponseEntity.ok(trips);
    }


    @DeleteMapping("/trips/{tripId}")
    public ResponseEntity<Void> deleteTrip(@PathVariable Long tripId) {
        tripService.deleteTrip(tripId);
        return ResponseEntity.noContent().build();
    }
}