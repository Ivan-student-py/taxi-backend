package com.taxi.trip.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TripCreateRequest(
        @NotNull(message = "Passenger ID is required")
        Long passengerId,

        @NotBlank(message = "Origin is required")
        String origin,

        @NotBlank(message = "Destination is required")
        String destination,

        @Min(value = 1, message = "Distance must be at least 1 km")
        double distance
) {}