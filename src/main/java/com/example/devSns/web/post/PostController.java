package com.example.devSns.web.post;

import com.example.devSns.domain.post.Post;
import com.example.devSns.service.post.PostService;
import com.example.devSns.web.post.dto.PostDtos.CreateReq;
import com.example.devSns.web.post.dto.PostDtos.UpdateReq;
import com.example.devSns.web.post.dto.PostDtos.Res;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    // Create
    @PostMapping
    public Res create(@Valid @RequestBody CreateReq req) {
        return toRes(postService.create(req));
    }

    // Read one
    @GetMapping("/{id}")
    public Res get(@PathVariable Long id) {
        return toRes(postService.get(id));
    }

    // Read all
    @GetMapping
    public List<Res> list() {
        return postService.list().stream().map(this::toRes).toList();
    }

    // Update
    @PutMapping("/{id}")
    public Res update(@PathVariable Long id, @Valid @RequestBody UpdateReq req) {
        return toRes(postService.update(id, req));
    }

    // Delete
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        postService.delete(id);
    }

    // Like
    @PostMapping("/{id}/like")
    public Res like(@PathVariable Long id) {
        return toRes(postService.like(id));
    }

    private Res toRes(Post p) {
        Res r = new Res();
        r.id = p.getId();
        r.content = p.getContent();
        r.username = p.getUsername();
        r.likes = p.getLikes();
        r.createdAt = p.getCreatedAt();
        r.updatedAt = p.getUpdatedAt();
        return r;
    }
}
