package com.example.devSns.Comment;

import com.example.devSns.Comment.Dto.CreateCommentDto;
import com.example.devSns.Comment.Dto.UpdateCommentDto;
import com.example.devSns.Post.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Transactional
    // 댓글 달기. 특정 게시글 밑에 작성 , 게시글 상세 조회 후 작성이 가능하도록
    public Long createComment(CreateCommentDto dto){
        Comment comment = dto.toEntity();
        Comment saved = commentRepository.save(comment);
        return saved.getId();
    }
    @Transactional
    // 댓글 목록 조회 - 게시글 밑에 나타나게끔. 근데 얘는 특정 포스트에 해당된 모든 댓글이 조회 되어야 할 터
    public List<Comment> getAllComments(Long postId){
        // repo에 인자로 전달된 postId를 가지는 comment 조회, 가져올 때 Dto 변환 시키기 JPQL
        return commentRepository.findAll();
    }
    @Transactional
    // 댓글 조회 - 특정 게시글 조회 시 댓글도 조회되게끔
    // 조회용 Dto 도 만들기, 내용, 작성자, 작성 일시가 나오면 될듯
    public Comment getCommentById(Long id){
        return commentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Comment not found"));
    }
    @Transactional
    // 댓글 수정
    // id 는 path로 , dto는 body로 전달받기
    public Comment updateComment(Long id, UpdateCommentDto dto){
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Comment not found"));
        comment.updateComment(dto);
        return commentRepository.save(comment);
    }
    @Transactional
    // 댓글 삭제
    public void deleteCommentById(Long id){
        commentRepository.deleteById(id);
    }

}
