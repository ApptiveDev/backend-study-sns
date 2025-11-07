package com.example.devSns.web.dto;

import jakarta.validation.constraints.NotBlank;

public record PostUpdateRequest(
        @NotBlank String title,
        String content
) {}
