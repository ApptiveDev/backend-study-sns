package com.example.devSns.task.dto;

import com.example.devSns.task.entity.Post;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// 게시글 생성 요청 DTO
// record는 final 필드를 가지며, 자동으로 생성자, Getter, equals/hashCode/toString 제공
public record PostCreateRequestDto(
        // DTO에서 유효성 검증을 담당
        @NotBlank(message = "작성자 명은 필수입니다.")
        String username,

        @NotBlank(message = "게시글 내용은 필수입니다.")
        @Size(max = 1000, message = "내용은 1000자를 초과할 수 없습니다.")
        String postContent
) {
    // Post 엔터티로 변환하는 메서드 (서비스 계층에서 사용)
    // record 필드 접근은 메서드 호출(getter) 방식으로 이루어짐.
    public Post toEntity(){
        // Post.builder()는 Post 클래스 내부에 정의되어 있어야 합니다.
        return Post.builder()
                .username(this.username()) // 필드 접근은 this.필드명()
                .postContent(this.postContent())
                .build();
    }

}
