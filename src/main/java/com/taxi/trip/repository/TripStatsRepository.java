package com.taxi.trip.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.taxi.trip.entity.Trip;
import com.taxi.trip.dto.TripStatsProjection;

@Repository
public interface TripStatsRepository extends JpaRepository<Trip, Long> {

    @Query(value = """
        SELECT 
            COUNT(*) as total_trips,
            COUNT(CASE WHEN status = 'COMPLETED' THEN 1 END) as completed_trips,
            ROUND(COALESCE(AVG(price), 0), 2) as average_price,
            COALESCE(SUM(price), 0) as total_revenue
        FROM trips
        """, nativeQuery = true)
    TripStatsProjection getTripStatsNative();
}