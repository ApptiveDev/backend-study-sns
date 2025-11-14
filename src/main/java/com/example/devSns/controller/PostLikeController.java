package com.example.devSns.controller;

import com.example.devSns.domain.PostLikes;
import com.example.devSns.service.PostLikesService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
public class PostLikeController extends LikeController<PostLikes> {
    public PostLikeController(PostLikesService service) {
        super(service);
    }
}
