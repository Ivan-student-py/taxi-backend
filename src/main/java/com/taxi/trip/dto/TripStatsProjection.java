package com.taxi.trip.dto;

import java.math.BigDecimal;

public interface TripStatsProjection {
    Long getTotalTrips();
    Long getCompletedTrips();
    BigDecimal getAveragePrice();
    BigDecimal getTotalRevenue();
}