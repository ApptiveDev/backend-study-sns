package com.example.devSns.Comment.Dto;

import com.example.devSns.Comment.Comment;

public record CreateCommentDto(
        String content,
        String author
) {

    public Comment toEntity(){
        return new Comment(content, author);

    }
}
