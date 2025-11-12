package com.example.devSns.service;

import com.example.devSns.domain.Member;
import com.example.devSns.domain.Post;
import com.example.devSns.dto.GenericDataDto;
import com.example.devSns.dto.post.PostCreateDto;
import com.example.devSns.dto.post.PostResponseDto;
import com.example.devSns.exception.InvalidRequestException;
import com.example.devSns.exception.NotFoundException;
import com.example.devSns.repository.CommentRepository;
import com.example.devSns.repository.MemberRepository;
import com.example.devSns.repository.PostRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;

    public PostService(PostRepository postRepository, CommentRepository commentRepository, MemberRepository memberRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public Long create(PostCreateDto postCreateDto) {
        Member member = memberRepository.findById(postCreateDto.memberId()).orElseThrow(()->new NotFoundException("member not found"));
        Post post = Post.create(postCreateDto.content(), member);
        return postRepository.save(post).getId();
    }

    public PostResponseDto findOne(Long id) {
        Post post = postRepository.findById(id).orElseThrow(()->new NotFoundException("post not found"));
//        Long comments = commentRepository.countCommentsByPostId(id);
        return PostResponseDto.from(post);
    }

    @Transactional
    public void delete(Long id) {
        Post post = postRepository.findById(id).orElseThrow(()->new NotFoundException("post not found"));
        postRepository.delete(post);
    }

    @Transactional
    public PostResponseDto updateContent(Long id, GenericDataDto<String> contentsDto) {
        if (contentsDto.data() == null || contentsDto.data().isEmpty())
            throw new InvalidRequestException("Invalid request.");

        Post post = postRepository.findById(id).orElseThrow(()->new NotFoundException("post not found"));
        post.setContent(contentsDto.data());

        return findOne(id);
    }

    public Slice<PostResponseDto> findAsSlice(Pageable pageable) {
        return postRepository.findPostSliceWithLikeCountAndCommentCount(pageable);
    }



}
