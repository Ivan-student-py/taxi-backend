package com.taxi.user.controller;

import com.taxi.shared.DriverStatus;
import com.taxi.user.dto.DriverCreateRequest;
import com.taxi.user.entity.Driver;
import com.taxi.user.entity.Passenger;
import com.taxi.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/passengers/{id}")
    @PreAuthorize("hasRole('PASSENGER') or hasRole('ADMIN')")
    public ResponseEntity<Passenger> getPassenger(@PathVariable Long id) {
        return userService.getPassenger(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/drivers/{id}")
    @PreAuthorize("hasRole('DRIVER') or hasRole('ADMIN')")
    public ResponseEntity<Driver> getDriver(@PathVariable Long id) {
        return userService.getDriver(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/drivers/{id}/status")
    @PreAuthorize("hasRole('DRIVER') or hasRole('ADMIN')")
    public ResponseEntity<Driver> updateDriverStatus(
            @PathVariable Long id,
            @RequestParam DriverStatus status) {
        try {
            return ResponseEntity.ok(userService.updateDriverStatus(id, status));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}