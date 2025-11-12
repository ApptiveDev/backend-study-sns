package com.example.devSns.controller;

import com.example.devSns.domain.CommentLikes;
import com.example.devSns.service.CommentLikesService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comments")
public class CommentLikeController extends LikeController<CommentLikes> {
    public CommentLikeController(CommentLikesService service) {
        super(service);
    }
}
