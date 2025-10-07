package com.example.devSns.controller;

import com.example.devSns.entity.PostEntity;
import com.example.devSns.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    // 생성자
    public PostController(PostService postService) {
        this.postService = postService;
    }

    // HTTP GET /posts
    @GetMapping
    public String list(Model model) {
        model.addAttribute("posts", postService.getAllPosts());
        return "list";
    }

    // HTTP GET /posts/new
    @GetMapping("/new")
    public String form(Model model) {
        model.addAttribute("post", new PostEntity());
        return "form";
    }

    // HTTP POST /posts
    @PostMapping
    public String create(@ModelAttribute PostEntity postEntity) {
        postService.createPost(postEntity);
        return "redirect:/posts";
    }

    // HTTP GET /posts/{id}
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("post", postService.getPost(id));
        return "detail";
    }

    // HTTP POST /posts/{id}/delete
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        postService.deletePost(id);
        return "redirect:/posts";
    }
}