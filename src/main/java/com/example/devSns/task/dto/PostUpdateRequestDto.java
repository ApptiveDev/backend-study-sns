package com.example.devSns.task.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PostUpdateRequestDto(
        @NotBlank(message = "수정할 내용은 필수입니다.")
        @Size(max = 1000, message = "내용은 1000자를 초과할 수 없습니다.")
        String postContent
) {
    // 수정 요청 DTO는 엔티티로 변환하지 않고, 서비스에서 엔티티의 update() 메서드를 호출하는 데 사용.
}
