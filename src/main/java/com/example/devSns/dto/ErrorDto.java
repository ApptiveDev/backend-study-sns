package com.example.devSns.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ErrorDto(
        @JsonProperty("error_message") String errorMessage
) {
}
