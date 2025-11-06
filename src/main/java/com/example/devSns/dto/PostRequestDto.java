package com.example.devSns.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostRequestDto {
    private Long id; // 추가 구조 단순화를 위한  -멘토 조언
    private String title;
    private String content;
}
/*
PostRequestDto 클라이언트 → 서버로 전달되는 데이터 구조
PostResponseDto 서버 → 클라이언트로 반환되는 데이터 구조
Post (Entity) DB 테이블과 직접 연결된 데이터 구조

* */