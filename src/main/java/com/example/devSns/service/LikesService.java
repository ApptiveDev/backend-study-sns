package com.example.devSns.service;

import com.example.devSns.dto.likes.LikesRequestDto;
import org.springframework.transaction.annotation.Transactional;


public abstract class LikesService<T>{

    @Transactional
    public Long like(LikesRequestDto likesRequestDto) {
        T likes = createLikes(likesRequestDto.targetId(), likesRequestDto.memberId());
        return saveLikes(likes);
    }

    @Transactional
    public void unlike(LikesRequestDto likesRequestDto) {
        T likes = findLikes(likesRequestDto.targetId(), likesRequestDto.memberId());
        deleteLikes(likes);
    }

    protected abstract T createLikes(Long targetId, Long memberId);
    protected abstract Long saveLikes(T likes);
    protected abstract T findLikes(Long targetId, Long memberId);
    protected abstract void deleteLikes(T likes);

}
