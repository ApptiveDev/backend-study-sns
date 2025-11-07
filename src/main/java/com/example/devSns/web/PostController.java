package com.example.devSns.web;

import com.example.devSns.domain.Post;
import com.example.devSns.service.PostService;
import com.example.devSns.web.dto.PostCreateRequest;
import com.example.devSns.web.dto.PostUpdateRequest;
import com.example.devSns.web.dto.PostResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService svc;

    public PostController(PostService svc) {
        this.svc = svc;
    }

    @GetMapping
    public List<PostResponse> getAll() {
        return svc.findAll().stream().map(PostResponse::from).toList();
    }

    @GetMapping("/{id}")
    public PostResponse getOne(@PathVariable Long id) {
        Post p = svc.findById(id);
        return PostResponse.from(p);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public PostResponse create(@Valid @RequestBody PostCreateRequest req) {
        Post saved = svc.create(req.title(), req.content());
        return PostResponse.from(saved);
    }

    @PutMapping("/{id}")
    public PostResponse update(@PathVariable Long id, @Valid @RequestBody PostUpdateRequest req) {
        Post updated = svc.update(id, req.title(), req.content());
        return PostResponse.from(updated);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        svc.delete(id);
    }
}
