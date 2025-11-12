package com.example.devSns.controller;

import com.example.devSns.dto.GenericDataDto;
import com.example.devSns.dto.likes.LikesRequestDto;
import com.example.devSns.dto.post.PostCreateDto;
import com.example.devSns.dto.post.PostResponseDto;
//import com.example.devSns.repository.PostRepository;
import com.example.devSns.dto.postLikes.PostLikesRequestDto;
import com.example.devSns.service.PostLikesService;
import com.example.devSns.service.PostService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final PostLikesService postLikesService;

    public PostController(PostService postService, PostLikesService postLikesService) {
        this.postService = postService;
        this.postLikesService = postLikesService;
    }

    @PostMapping
    public ResponseEntity<GenericDataDto<Long>> create(@RequestBody @Valid PostCreateDto postCreateDto) {
        Long id = postService.create(postCreateDto);

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
    public ResponseEntity<Slice<PostResponseDto>> getAsPaginated(
            @PageableDefault(
                    size = 15,
                    sort = "id",
                    direction = Sort.Direction.DESC
            )
            Pageable pageable
    ) {
        Slice<PostResponseDto> posts = postService.findAsSlice(pageable);
        return ResponseEntity.ok().body(posts);
    }


    @PatchMapping("/{id}/contents")
    public ResponseEntity<PostResponseDto> contents(
            @PathVariable @Positive Long id, @RequestBody @Valid GenericDataDto<String> contentsDto) {

        PostResponseDto post = postService.updateContent(id, contentsDto);
        return ResponseEntity.ok().body(post);
    }

}
