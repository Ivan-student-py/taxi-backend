package com.taxi.trip.controller;

import com.taxi.trip.dto.TripCreateRequest;
import com.taxi.trip.dto.TripRatingRequest;
import com.taxi.trip.entity.Trip;
import com.taxi.trip.enums.TripStatus;
import com.taxi.trip.service.TripService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/trips")
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;

    @PostMapping
    @PreAuthorize("hasRole('PASSENGER')")
    public ResponseEntity<Trip> createTrip(@Valid @RequestBody TripCreateRequest req) {
        return ResponseEntity.ok(tripService.createTrip(req));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Trip> getTrip(@PathVariable Long id) {
        return ResponseEntity.ok(tripService.getTrip(id));
    }

    @GetMapping
    public ResponseEntity<List<Trip>> getTripsByPassenger(@RequestParam Long passengerId) {
        return ResponseEntity.ok(tripService.getTripsByPassenger(passengerId));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('DRIVER') or hasRole('ADMIN')")
    public ResponseEntity<Trip> updateStatus(
            @PathVariable Long id,
            @RequestParam TripStatus status) {
        return ResponseEntity.ok(tripService.updateStatus(id, status));
    }

    @GetMapping("/driver/{driverId}")
    @PreAuthorize("hasRole('DRIVER') or hasRole('ADMIN')")
    public ResponseEntity<List<Trip>> getTripsByDriver(@PathVariable Long driverId) {
        return ResponseEntity.ok(tripService.getTripsByDriver(driverId));
    }

    @PatchMapping("/{id}/rate")
    @PreAuthorize("hasRole('PASSENGER')")
    public ResponseEntity<Trip> rateTrip(
            @PathVariable Long id,
            @Valid @RequestBody TripRatingRequest req) {
        return ResponseEntity.ok(tripService.rateTrip(id, req));
    }
}