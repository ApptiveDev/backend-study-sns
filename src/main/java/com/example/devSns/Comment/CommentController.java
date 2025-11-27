package com.example.devSns.Comment;

import com.example.devSns.Comment.Dto.CreateCommentDto;
import com.example.devSns.Comment.Dto.UpdateCommentDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("posts/{post_id}/comments")
    public ResponseEntity<?> postComment(
            @Valid
            @RequestBody CreateCommentDto dto,
            @PathVariable Long post_id) {

        try{
            commentService.createComment(post_id, dto);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }catch (EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }


    }

    @PostMapping("posts/{post_id}/comments/{comment_id}")
    public ResponseEntity<?> postReplyComment(
            @Valid
            @RequestBody CreateCommentDto dto,
            @PathVariable Long post_id,
            @PathVariable Long comment_id
    ) {
        try{
            commentService.createReplyComment(post_id, comment_id, dto);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }catch (EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("posts/{post_id}/comments")
    public ResponseEntity<List<Comment>> getComments(@PathVariable("post_id") Long post_id) {
        return ResponseEntity.ok().body(commentService.getAllComments(post_id));

    }
    @GetMapping("posts/{post_id}/comments/{comment_id}")
    public ResponseEntity<Comment> getCommentsByPostId(
            @PathVariable("post_id") Long post_id,
            @PathVariable("comment_id") Long comment_id) {
        try{
            return ResponseEntity.ok(commentService.getCommentById(post_id, comment_id));

        }catch (EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }


    @PatchMapping("/comments/{comment_id}")
    public ResponseEntity<Comment> updateComment(
            @PathVariable("comment_id") Long comment_id,
            @Valid
            @RequestBody UpdateCommentDto dto) {
            try{
                return ResponseEntity.ok().body(commentService.updateComment(comment_id, dto));
            }catch (EntityNotFoundException e){
                return ResponseEntity.notFound().build();
            }


    }
    @DeleteMapping("comments/{comment_id}")
    public ResponseEntity<?> deleteComment(@PathVariable("comment_id") Long comment_id) {
        try{
            commentService.deleteCommentById(comment_id);
            return ResponseEntity.noContent().build();
        }
        catch (EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }


}
