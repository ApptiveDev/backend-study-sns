package com.example.devSns.web.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public class PostDtos {

    public static class CreateReq {
        @NotBlank @Size(max = 1000)
        public String content;
        @NotBlank
        public String username;
    }

    public static class UpdateReq {
        @NotBlank @Size(max = 1000)
        public String content;
    }

    public static class Res {
        public Long id;
        public String content;
        public String username;
        public Integer likes;
        public LocalDateTime createdAt;
        public LocalDateTime updatedAt;
    }
}
