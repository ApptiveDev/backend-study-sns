package com.example.devSns.service;

import com.example.devSns.dto.likes.LikesRequestDto;
import com.example.devSns.dto.likes.LikesResponseDto;
import com.example.devSns.dto.member.MemberResponseDto;
import com.example.devSns.exception.InvalidRequestException;
import com.example.devSns.exception.NotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


public abstract class LikesService<T>{

    @Transactional
    public void like(LikesRequestDto likesRequestDto) {
        if (findLikes(likesRequestDto.targetId(), likesRequestDto.memberId()).isPresent())
            throw new InvalidRequestException("target already liked");
        T likes = createLikes(likesRequestDto.targetId(), likesRequestDto.memberId());
        saveLikes(likes);
    }

    @Transactional
    public void unlike(LikesRequestDto likesRequestDto) {
        Optional<T> likes = findLikes(likesRequestDto.targetId(), likesRequestDto.memberId());
        likes.orElseThrow(()->new NotFoundException("target not liked"));
        deleteLikes(likes.get());
    }


    public abstract Slice<MemberResponseDto> findWhoLiked(Pageable pageable, Long targetId);

    protected abstract T createLikes(Long targetId, Long memberId);
    protected abstract void saveLikes(T likes);
    protected abstract Optional<T> findLikes(Long targetId, Long memberId);
    protected abstract void deleteLikes(T likes);
//    protected abstract boolean isAlreadyLiked(T likes);
}
