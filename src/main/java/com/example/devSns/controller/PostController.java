package com.example.devSns.controller;

import com.example.devSns.dto.GenericDataDto;
import com.example.devSns.dto.PaginatedDto;
import com.example.devSns.dto.post.PostCreateDto;
import com.example.devSns.dto.post.PostResponseDto;
import com.example.devSns.exception.InvalidRequestException;
//import com.example.devSns.repository.PostRepository;
import com.example.devSns.service.PostService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<GenericDataDto<Long>> create(@RequestBody @Valid PostCreateDto postCreateDto) {
        Long id = postService.join(postCreateDto);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(uri).body(new GenericDataDto<>(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> getOne(@PathVariable @Positive Long id) {
        PostResponseDto post = postService.findOne(id);
        return ResponseEntity.ok().body(post);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @Positive Long id) {
        postService.delete(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping
    public ResponseEntity<PaginatedDto<List<PostResponseDto>>> getAsPaginated(
            @RequestParam(required = false)
//            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            Long before
    ) {
        PaginatedDto<List<PostResponseDto>> posts = postService.findAsPaginated(new GenericDataDto<>(before));
        return ResponseEntity.ok().body(posts);
    }

    @PostMapping("/{id}/likes")
    public ResponseEntity<PostResponseDto> like(@PathVariable @Positive Long id) {
        postService.like(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/contents")
    public ResponseEntity<PostResponseDto> contents(
            @PathVariable @Positive Long id, @RequestBody @Valid GenericDataDto<String> contentsDto) {

        PostResponseDto post = postService.updateContent(id, contentsDto);
        return ResponseEntity.ok().body(post);
    }

}
