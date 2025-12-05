package com.example.devSns.service;

import com.example.devSns.dto.PostCreateRequest;
import com.example.devSns.dto.PostUpdateRequest;
import com.example.devSns.entity.Member;
import com.example.devSns.entity.Post;
import com.example.devSns.repository.MemberRepository;
import com.example.devSns.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository PostRepository;
    private final MemberRepository MemberRepository;

    public PostService(PostRepository postRepository,MemberRepository memberRepository) {
        this.PostRepository = postRepository;
        this.MemberRepository = memberRepository;
    }

    public Page<Post> FindAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return PostRepository.findAll(pageable);
    }

    public Post FindById(Long id){
        return PostRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Post not found"));
    }

    @Transactional
    public Post CreatePost(PostCreateRequest request){
        Member member = MemberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("member not found"));

        Post post = Post.create(
                request.getContent(),
                member   // ✔ member 연결
        );

        return PostRepository.save(post);
    }

    @Transactional
    public Post UpdatePost(Long id, PostUpdateRequest request) {
        Post existingPost = PostRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("post not found"));
        existingPost.updateContent(request.getContent());
        return existingPost;
    }

    @Transactional
    public void delete(Long id){
        PostRepository.deleteById(id);
    }
}
