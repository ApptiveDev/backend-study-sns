package com.example.devSns.Comment.Dto;

import com.example.devSns.Comment.Comment;
import com.example.devSns.Post.Post;

public record CreateCommentDto(
        String content,
        String author
) {
    public Comment toEntity() {
        return new Comment(content, author);

    }

}
