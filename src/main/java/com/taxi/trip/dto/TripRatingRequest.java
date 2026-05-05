package com.taxi.trip.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record TripRatingRequest(
        @Min(1) @Max(5) int rating
) {}