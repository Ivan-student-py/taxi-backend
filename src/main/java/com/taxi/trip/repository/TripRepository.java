package com.taxi.trip.repository;

import com.taxi.trip.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Long> {
    List<Trip> findByPassengerId(Long passengerId);
    List<Trip> findByDriverId(Long driverId);

    boolean existsByPassengerIdAndStatusIn(Long passengerId,
                                           java.util.List<com.taxi.trip.enums.TripStatus> statuses);
}