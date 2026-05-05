package com.taxi.trip.service;

import com.taxi.shared.DriverStatus;
import com.taxi.trip.dto.TripCreateRequest;
import com.taxi.trip.dto.TripRatingRequest;
import com.taxi.trip.entity.Trip;
import com.taxi.trip.enums.TripStatus;
import com.taxi.trip.repository.TripRepository;
import com.taxi.user.repository.DriverRepository;
import com.taxi.user.repository.PassengerRepository;
import com.taxi.shared.cache.RedisDriverCache;
import com.taxi.user.entity.Driver;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class TripService {

    private final TripRepository tripRepo;
    private final DriverRepository driverRepo;
    private final PassengerRepository passengerRepo;
    private final RedisDriverCache driverCache;

    @Value("${taxi.tariff.per-km}")
    private double tariffPerKm;

    @Transactional
    public Trip createTrip(TripCreateRequest req) {
        if (!passengerRepo.existsById(req.passengerId())) {
            throw new EntityNotFoundException("Passenger not found");
        }

        Driver driver = null;

        String cachedDriverId = driverCache.getRandomFreeDriverId();
        if (cachedDriverId != null) {
            log.info("[CACHE HIT] Found candidate driver ID={} in Redis", cachedDriverId);
            Long id = Long.parseLong(cachedDriverId);
            driver = driverRepo.findById(id).orElse(null);

            if (driver == null || driver.getStatus() != DriverStatus.FREE) {
                log.warn("[CACHE STALE] Driver {} is invalid. Removing from cache.", id);
                driverCache.removeDriver(id);
                driver = null;
            } else {
                log.info("[ASSIGNED] Driver {} confirmed and selected from Redis cache.", driver.getId());
            }
        }

        if (driver == null) {
            log.info("[CACHE MISS / FALLBACK] Executing SELECT ... FOR UPDATE in PostgreSQL");
            List<Driver> freeDrivers = driverRepo.findDriversByStatusForUpdate(DriverStatus.FREE);
            if (freeDrivers.isEmpty()) {
                throw new IllegalStateException("No free drivers available");
            }
            driver = freeDrivers.get(0);
            log.info("[ASSIGNED] Driver {} selected via DB fallback. Warming up cache.", driver.getId());
            driverCache.addFreeDriver(driver.getId());
        }

        BigDecimal price = BigDecimal.valueOf(req.distance() * tariffPerKm);
        Trip trip = Trip.builder()
                .passengerId(req.passengerId())
                .driverId(driver.getId())
                .status(TripStatus.ASSIGNED)
                .origin(req.origin())
                .destination(req.destination())
                .distance(req.distance())
                .price(price)
                .build();

        driver.setStatus(DriverStatus.BUSY);
        driverRepo.save(driver);
        driverCache.removeDriver(driver.getId());
        log.info("[CACHE INVALIDATE] Removed driver {} from Redis (status: BUSY)", driver.getId());

        return tripRepo.save(trip);
    }

    public Trip getTrip(Long id) {
        return tripRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Trip not found"));
    }

    public List<Trip> getTripsByPassenger(Long passengerId) {
        return tripRepo.findByPassengerId(passengerId);
    }

    @Transactional
    public Trip updateStatus(Long id, TripStatus newStatus) {
        Trip trip = tripRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Trip not found"));

        trip.setStatus(newStatus);

        if (newStatus == TripStatus.COMPLETED || newStatus == TripStatus.CANCELLED) {
            if (trip.getDriverId() != null) {
                com.taxi.user.entity.Driver driver = driverRepo.findById(trip.getDriverId())
                        .orElseThrow(() -> new EntityNotFoundException("Driver not found"));
                driver.setStatus(DriverStatus.FREE);
                driverRepo.save(driver);
            }
        }
        return tripRepo.save(trip);
    }

    @Transactional
    public Trip rateTrip(Long id, TripRatingRequest req) {
        Trip trip = tripRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Trip not found"));

        if (trip.getStatus() != TripStatus.COMPLETED) {
            throw new IllegalStateException("Can rate only completed trips");
        }

        trip.setRating(req.rating());
        return tripRepo.save(trip);
    }
}