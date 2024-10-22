package com.spring.app.urlshorter.controller.user;

import jakarta.validation.constraints.NotBlank;

public record AuthRequest(
        @NotBlank
        String userName,
        @NotBlank
        String password
) {}
