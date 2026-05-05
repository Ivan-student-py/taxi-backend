package com.taxi.user.service;

import com.taxi.shared.Role;
import com.taxi.security.JwtUtils;
import com.taxi.shared.cache.RedisDriverCache;
import com.taxi.shared.DriverStatus;
import com.taxi.user.dto.*;
import com.taxi.user.entity.Driver;
import com.taxi.user.entity.Passenger;
import com.taxi.user.repository.DriverRepository;
import com.taxi.user.repository.PassengerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final PassengerRepository passengerRepo;
    private final DriverRepository driverRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final RedisDriverCache driverCache;

    public UserService(PassengerRepository passengerRepo,
                       DriverRepository driverRepo,
                       PasswordEncoder passwordEncoder,
                       JwtUtils jwtUtils,
                       RedisDriverCache driverCache) {
        this.passengerRepo = passengerRepo;
        this.driverRepo = driverRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.driverCache = driverCache;
    }

    @Transactional
    public AuthResponse registerPassenger(PassengerCreateRequest req) {
        if (passengerRepo.existsByEmail(req.email()) || driverRepo.existsByEmail(req.email())) {
            throw new IllegalArgumentException("Email already registered.");
        }
        Passenger passenger = Passenger.builder()
                .name(req.name())
                .email(req.email())
                .phone(req.phone())
                .passwordHash(passwordEncoder.encode(req.password()))
                .build();
        passengerRepo.save(passenger);
        String token = jwtUtils.generateToken(passenger.getId(), passenger.getEmail(), Role.PASSENGER.name());
        return new AuthResponse(passenger.getId(), passenger.getEmail(), passenger.getName(), Role.PASSENGER, token);
    }

    @Transactional
    public AuthResponse registerDriver(DriverCreateRequest req) {
        if (passengerRepo.existsByEmail(req.email()) || driverRepo.existsByEmail(req.email())) {
            throw new IllegalArgumentException("Email already registered.");
        }
        if (driverRepo.existsByLicenseNumber(req.licenseNumber())) {
            throw new IllegalArgumentException("The license number is already in use.");
        }
        Driver driver = Driver.builder()
                .name(req.name())
                .email(req.email())
                .phone(req.phone())
                .licenseNumber(req.licenseNumber())
                .status(req.status() != null ? req.status() : DriverStatus.OFFLINE)
                .passwordHash(passwordEncoder.encode(req.password()))
                .build();
        driverRepo.save(driver);
        String token = jwtUtils.generateToken(driver.getId(), driver.getEmail(), Role.DRIVER.name());
        return new AuthResponse(driver.getId(), driver.getEmail(), driver.getName(), Role.DRIVER, token);
    }

    public AuthResponse login(AuthRequest req) {
        Optional<Passenger> passengerOpt = passengerRepo.findByEmail(req.email());
        if (passengerOpt.isPresent() && passwordEncoder.matches(req.password(), passengerOpt.get().getPasswordHash())) {
            Passenger p = passengerOpt.get();
            String token = jwtUtils.generateToken(p.getId(), p.getEmail(), Role.PASSENGER.name());
            return new AuthResponse(p.getId(), p.getEmail(), p.getName(), Role.PASSENGER, token);
        }
        Optional<Driver> driverOpt = driverRepo.findByEmail(req.email());
        if (driverOpt.isPresent() && passwordEncoder.matches(req.password(), driverOpt.get().getPasswordHash())) {
            Driver d = driverOpt.get();
            String token = jwtUtils.generateToken(d.getId(), d.getEmail(), Role.DRIVER.name());
            return new AuthResponse(d.getId(), d.getEmail(), d.getName(), Role.DRIVER, token);
        }
        throw new IllegalArgumentException("Wrong email or password.");
    }

    public Optional<Passenger> getPassenger(Long id) {
        return passengerRepo.findById(id);
    }

    public Optional<Driver> getDriver(Long id) {
        return driverRepo.findById(id);
    }

    @Transactional
    public Driver updateDriverStatus(Long id, DriverStatus newStatus) {
        Driver driver = driverRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Driver is not found."));

        driver.setStatus(newStatus);
        driverRepo.save(driver);

        if (newStatus == DriverStatus.FREE) {
            driverCache.addFreeDriver(id);
        } else {
            driverCache.removeDriver(id);
        }

        return driver;
    }
}