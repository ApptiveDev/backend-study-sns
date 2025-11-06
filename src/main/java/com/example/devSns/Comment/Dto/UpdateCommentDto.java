package com.example.devSns.Comment.Dto;

import com.example.devSns.Comment.Comment;

public record UpdateCommentDto(
        String content,
        String author) {

    public Comment toEntity(){
        return new Comment(content, author);
    }
}
