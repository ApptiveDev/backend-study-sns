package com.example.devSns;

import com.example.devSns.dto.PostResponse;
import com.example.devSns.entities.Posts;
import com.example.devSns.entities.Replies;
import com.example.devSns.entities.Users;
import com.example.devSns.repositories.PostRepository;
import com.example.devSns.repositories.ReplyRepository;
import com.example.devSns.repositories.UserRepository;
import com.example.devSns.services.PostService;
import com.example.devSns.services.ReplyService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
public class searchTest {
    @Autowired
    ReplyRepository replyRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private ReplyService replyService;

    @Autowired
    private PostService postService;

    private long postId;
    private long replyId;

    @BeforeEach
    void setUp() {
        replyRepository.deleteAll();
        postRepository.deleteAll();
        userRepository.deleteAll();

        Users user1 = Users.builder()
                .age(28)
                .username("Java")
                .build();

        userRepository.save(user1);

        Users user2 = Users.builder()
                .age(57)
                .username("Unix")
                .build();
        userRepository.save(user2);

        Posts post = Posts.builder()
                .content("Hello, Java!")
                .users(user1)
                .build();
        postRepository.save(post);
        postId = post.getId();

        Replies reply = Replies.builder()
                .posts(post)
                .reply("Me too")
                .createAt(LocalDateTime.now())
                .users(user2)
                .build();

        replyRepository.save(reply);
        replyId = reply.getId();
    }

    @Test
    public void likePostTest() {
        List<PostResponse> list = postService.findByContent("Hello");
        Assertions.assertEquals(1, list.size());
    }
}
