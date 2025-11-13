package com.example.devSns.Heart;

import com.example.devSns.Member.Gender;
import com.example.devSns.Member.Member;
import com.example.devSns.Post.Post;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HeartEntityTest {

    @Test
    @DisplayName("toggleLike 테스트 - NONE -> LIKE")
    void toggleLike_noneToLike() {
        Post post = new Post("content", "writer", 0L);
        Member member = new Member("nick", "email", "pwd", Gender.MALE, 28);

        Heart heart = new Heart(post, member, LikeStatus.NONE);

        heart.toggleLike();

        assertEquals(LikeStatus.LIKE, heart.getLike());
    }

    @Test
    @DisplayName("toggleLike 테스트 - LIKE -> NONE")
    void toggleLike_likeToNone() {
        Post post = new Post("content", "writer", 0L);
        Member member = new Member("nick", "email", "pwd", Gender.MALE, 28);

        Heart heart = new Heart(post, member, LikeStatus.LIKE);

        heart.toggleLike();

        assertEquals(LikeStatus.NONE, heart.getLike());
    }
}

