package com.example.devSns.dto.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

public record PostCreateDto(
        @NotNull @NotEmpty String content,
        @NotNull @NotEmpty @NotBlank String user_name
) {
}
