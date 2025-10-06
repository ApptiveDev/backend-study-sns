package com.example.devSns.dto;

import java.time.LocalDateTime;

public record PaginatedDto<T>(T data, LocalDateTime nextQueryCriteria) {
}
