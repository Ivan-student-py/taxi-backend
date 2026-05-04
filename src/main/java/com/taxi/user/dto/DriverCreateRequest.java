package com.taxi.user.dto;

import com.taxi.shared.DriverStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record DriverCreateRequest(
        @NotBlank(message = "Name is required.")
        String name,

        @Email(message = "Wrong format of email.")
        @NotBlank(message = "Email is required.")
        String email,

        String phone,

        @NotBlank(message = "License number is required.")
        String licenseNumber,

        DriverStatus status,

        @NotBlank(message = "Password is required.")
        String password
) {}