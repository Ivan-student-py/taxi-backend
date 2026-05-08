package com.taxi.trip.service;

import com.taxi.trip.dto.TripStatsResponse;
import com.taxi.trip.repository.TripStatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final TripStatsRepository statsRepo;

    public TripStatsResponse getTripStats() {
        var projection = statsRepo.getTripStatsNative();

        return new TripStatsResponse(
                projection.getTotalTrips() != null ? projection.getTotalTrips() : 0L,
                projection.getCompletedTrips() != null ? projection.getCompletedTrips() : 0L,
                projection.getAveragePrice() != null ? projection.getAveragePrice() : java.math.BigDecimal.ZERO,
                projection.getTotalRevenue() != null ? projection.getTotalRevenue() : java.math.BigDecimal.ZERO
        );
    }
}