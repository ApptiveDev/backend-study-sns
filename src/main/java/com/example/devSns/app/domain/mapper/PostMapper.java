package com.example.devSns.app.domain.mapper;

import com.example.devSns.app.data.jpa.entity.PostEntity;
import com.example.devSns.app.domain.model.Post;

public class PostMapper {

    public static Post toDomain(PostEntity entity) {
        if (entity == null) return null;
        return new Post(
                entity.getId(),
                entity.getContent(),
                entity.getAuthorName(),
                entity.getLikes(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public static PostEntity toEntity(Post domain) {
        if (domain == null) return null;
        PostEntity entity = new PostEntity(domain.getContent(), domain.getAuthorName());
        // id, createdAt, updatedAt은 JPA가 관리하므로 따로 세팅하지 않음
        return entity;
    }
}
