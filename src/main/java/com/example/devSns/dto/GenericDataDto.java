package com.example.devSns.dto;

import jakarta.validation.constraints.NotNull;

public record GenericDataDto<T>(
        @NotNull T data
) {
}
