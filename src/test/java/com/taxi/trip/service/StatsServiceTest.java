package com.taxi.trip.service;

import com.taxi.trip.dto.TripStatsProjection;
import com.taxi.trip.dto.TripStatsResponse;
import com.taxi.trip.repository.TripStatsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatsServiceTest {

    @Mock
    private TripStatsRepository statsRepo;

    @InjectMocks
    private StatsService statsService;

    @Test
    void getTripStats_ShouldMapCorrectly_WhenDataExists() {
        TripStatsProjection proj = mock(TripStatsProjection.class);
        when(proj.getTotalTrips()).thenReturn(4L);
        when(proj.getCompletedTrips()).thenReturn(1L);
        when(proj.getAveragePrice()).thenReturn(new BigDecimal("525.00"));
        when(proj.getTotalRevenue()).thenReturn(new BigDecimal("2100.00"));
        when(statsRepo.getTripStatsNative()).thenReturn(proj);

        TripStatsResponse res = statsService.getTripStats();

        assertNotNull(res);
        assertEquals(4L, res.totalTrips());
        assertEquals(1L, res.completedTrips());
        assertEquals(new BigDecimal("525.00"), res.averagePrice());
    }

    @Test
    void getTripStats_ShouldReturnDefaults_WhenNull() {
        TripStatsProjection proj = mock(TripStatsProjection.class);
        when(proj.getTotalTrips()).thenReturn(null);
        when(proj.getCompletedTrips()).thenReturn(null);
        when(proj.getAveragePrice()).thenReturn(null);
        when(proj.getTotalRevenue()).thenReturn(null);
        when(statsRepo.getTripStatsNative()).thenReturn(proj);

        TripStatsResponse res = statsService.getTripStats();

        assertEquals(0L, res.totalTrips());
        assertEquals(BigDecimal.ZERO, res.averagePrice());
    }
}