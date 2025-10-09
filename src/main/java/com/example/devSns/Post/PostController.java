package com.example.devSns.Post;

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
    public void createPost(@RequestBody Post post) {
        postService.save(post);
    }

    @GetMapping()
    public List<Post> getAllPosts() {
        return postService.findAll();
    }
    @GetMapping("/{post_id}")
    public Post getPostById(@PathVariable(name ="post_id") long id) {
        return postService.findById(id);
    }

    @PutMapping
    public void updatePost(@RequestBody Post post) {
        postService.update(post);
    }
    @DeleteMapping("/{post_id}")
    public void deletePostById(@PathVariable(name ="post_id") long id) {
        postService.delete(id);
    }


}
