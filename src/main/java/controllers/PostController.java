package controllers;

import dto.Post;
import dto.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.PostService;

import java.util.List;

@RestController
@RequestMapping("/sns")
@RequiredArgsConstructor
public class PostController {
    private PostService postService;

    @GetMapping("/show")
    public ResponseEntity<List<PostResponse>> showPosts() {
        List<PostResponse> posts = postService.findAll();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/show/{username}")
    public ResponseEntity<PostResponse> showPost(@PathVariable String username) {
        PostResponse post = postService.findByUsername(username);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<PostResponse> addPost(@RequestBody Post post) {
        PostResponse postResponse = postService.save(post);
        return new ResponseEntity<>(postResponse, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<PostResponse> updatePost(@RequestBody Post post) {
        PostResponse postResponse = postService.update(post);
        return new ResponseEntity<>(postResponse, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deletePost(@RequestBody Post post) {
        postService.delete(post);
        return new ResponseEntity<>("Post deleted", HttpStatus.OK);
    }
}
