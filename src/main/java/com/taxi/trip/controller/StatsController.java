package com.taxi.trip.controller;

import com.taxi.trip.dto.TripStatsResponse;
import com.taxi.trip.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stats")
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @GetMapping("/trips")
    public ResponseEntity<TripStatsResponse> getTripStats() {
        return ResponseEntity.ok(statsService.getTripStats());
    }
}