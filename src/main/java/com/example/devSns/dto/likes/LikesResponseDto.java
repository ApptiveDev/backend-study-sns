package com.example.devSns.dto.likes;

import com.example.devSns.dto.member.MemberResponseDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.Slice;

public record LikesResponseDto(
        @JsonProperty("target_id") Long targetId,
        @JsonProperty("members") Slice<MemberResponseDto> members
        ) {
}
