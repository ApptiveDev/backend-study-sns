package com.example.devSns.dto;

import lombok.Builder;

@Builder
public record JwtDTO(
    String token
) {}
