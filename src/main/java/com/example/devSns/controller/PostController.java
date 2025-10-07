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
        return "redirect:/posts"; // 글 생성 후 돌아올 주소
    }

    // HTTP GET /posts/{id}
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("post", postService.getPost(id));
        return "detail";
    }

    // HTTP GET /posts/{id}/edit
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        PostEntity postEntity = postService.getPost(id);
        model.addAttribute("post", postEntity);
        return "edit";
    }

    // HTTP POST /posts/{id}/update
    @PostMapping("/{id}/update")
    public String update(@PathVariable Long id, @ModelAttribute PostEntity updatedPost) {
        postService.updatePost(id, updatedPost);
        return "redirect:/posts/" + id; // 수정 후 돌아올 주소
    }

    // HTTP POST /posts/{id}/delete
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        postService.deletePost(id);
        return "redirect:/posts"; // 삭제 후 돌아올 주소
    }
}