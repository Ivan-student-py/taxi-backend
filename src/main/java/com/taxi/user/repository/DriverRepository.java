package com.taxi.user.repository;

import com.taxi.shared.DriverStatus;
import com.taxi.user.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface DriverRepository extends JpaRepository<Driver, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT d FROM Driver d WHERE d.status = :status")
    List<Driver> findDriversByStatusForUpdate(@Param("status") com.taxi.shared.DriverStatus status);
    Optional<Driver> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByLicenseNumber(String licenseNumber);
    List<Driver> findByStatus(DriverStatus status);
}