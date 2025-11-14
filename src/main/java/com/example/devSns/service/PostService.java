package com.example.devSns.service;

import com.example.devSns.dto.PostCreateRequest;
import com.example.devSns.dto.PostResponse;
import com.example.devSns.dto.PostUpdateRequest;
import com.example.devSns.entity.Member;
import com.example.devSns.entity.Post;
import com.example.devSns.repository.MemberRepository;
import com.example.devSns.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public PostService(PostRepository postRepository,MemberRepository memberRepository) {
        this.postRepository = postRepository;
        this.memberRepository = memberRepository;
    }

    public List<Post> findAll(){
        return postRepository.findAll();
    }

    public Post findById(Long id){
        return postRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Post not found"));
    }

    @Transactional
    public Post createPost(PostCreateRequest request){
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("member not found"));

        Post post = Post.create(
                request.getContent(),
                member   // ✔ member 연결
        );

        return postRepository.save(post);
    }

    @Transactional
    public Post updatePost(Long id, PostUpdateRequest request) {
        Post existingPost = postRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("post not found"));
        existingPost.updateContent(request.getContent());
        return existingPost;
    }

    @Transactional
    public void delete(Long id){
        postRepository.deleteById(id);
    }
}
