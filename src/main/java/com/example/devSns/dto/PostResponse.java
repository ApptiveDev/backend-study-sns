package com.example.devSns.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
public record PostResponse(
   Long id,
   String username,
   String content,
   Integer like,
   LocalDateTime createAt,
   LocalDateTime updateAt
) {}
