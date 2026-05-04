package com.taxi.user.dto;

import com.taxi.shared.Role;

public record AuthResponse(
        Long id,
        String email,
        String name,
        Role role,
        String token
) {}