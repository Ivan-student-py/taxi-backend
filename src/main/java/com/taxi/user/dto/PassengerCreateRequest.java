package com.taxi.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record PassengerCreateRequest(
        @NotBlank(message = "Name is required.")
        String name,

        @Email(message = "Wrong format of email")
        @NotBlank(message = "Email is required.")
        String email,

        String phone,

        @NotBlank(message = "Password is required.")
        String password
) {}