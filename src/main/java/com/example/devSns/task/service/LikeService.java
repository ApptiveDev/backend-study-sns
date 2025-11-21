package com.example.devSns.task.service;

import com.example.devSns.task.entity.Like;
import com.example.devSns.task.entity.Member;
import com.example.devSns.task.entity.Post;
import com.example.devSns.task.repository.LikeRepository;
import com.example.devSns.task.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class LikeService {
    // 의존선 주입
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;

    public LikeService(LikeRepository likeRepository, PostRepository postRepository) {
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
    }

    // 좋아요 기능 구현
    public void toggleLike(Member member, Long postId){
        // 게시글 조회
        Post post = findPostByPostId(postId);

        // 게시글이 눌렸는지 확인
        Optional<Like> optionalLike = likeRepository.findByMemberIdAndPostId(member.getId(), postId);

        if(optionalLike.isPresent()){
            // 좋아요를 눌렸을 경우 삭제(취소 로직)
            likeRepository.delete(optionalLike.get());
            post.removeLike();
        } else{
            // 없을 경우 생성 및 저장(생성 로직)
            Like like = new Like(member,post);
            likeRepository.save(like);
            post.addLike();
        }
    }

    private Post findPostByPostId(Long postId){
        return postRepository.findById(postId)
                .orElseThrow(()->new NoSuchElementException("게시물을 찾을 수 없습니다."));
    }

}
