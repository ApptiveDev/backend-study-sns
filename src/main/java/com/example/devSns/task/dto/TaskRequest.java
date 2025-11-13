package com.example.devSns.task.dto;

import java.time.LocalDate;
import com.example.devSns.task.Task; // ✅ 이 import 꼭 필요!

public record TaskRequest(
        String title,
        String description,
        LocalDate dueDate,
        Integer priority,
        Task.Status status,   // ✅ enum 타입 명시
        Long memberId
) {}
