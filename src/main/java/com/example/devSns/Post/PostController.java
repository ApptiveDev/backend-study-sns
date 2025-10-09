package com.example.devSns.Post;

import com.example.devSns.Post.DTO.AddPostRequestDTO;
import com.example.devSns.Post.DTO.GetPostResponseDTO;
import com.example.devSns.Post.DTO.UpdatePostRequestDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping()
    public ResponseEntity<Void> createPost(@RequestBody AddPostRequestDTO DTO) {
        postService.createPost(DTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping()
    public List<Post> getAllPosts() {
        return postService.findAll();
    }
    @GetMapping("/{post_id}")
    public GetPostResponseDTO getPostById(@PathVariable(name ="post_id") long id) {
        return postService.findById(id);
    }

    @PatchMapping("/{post_id}")
    public void updatePost(
            @RequestBody UpdatePostRequestDTO DTO,
            @PathVariable(name ="post_id") Long id) {
        postService.updatePost(id, DTO);
    }

    @DeleteMapping("/{post_id}")
    public ResponseEntity<Void> deletePostById(@PathVariable(name ="post_id") long id) {
        postService.delete(id);
        return ResponseEntity.noContent().build();
    }


}
