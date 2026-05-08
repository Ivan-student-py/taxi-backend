package com.taxi.trip.dto;

import java.math.BigDecimal;

public record TripStatsResponse(
        Long totalTrips,
        Long completedTrips,
        BigDecimal averagePrice,
        BigDecimal totalRevenue
) {}