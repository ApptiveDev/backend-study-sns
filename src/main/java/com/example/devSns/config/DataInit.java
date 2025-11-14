package com.example.devSns.config;

import com.example.devSns.entity.MemberEntity;
import com.example.devSns.repository.MemberRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DataInit {

    private final MemberRepository memberRepository;

    public DataInit(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @PostConstruct
    public void init() {
        // Member가 없으면 테스트용 계정 자동 생성
        if (memberRepository.count() == 0) {
            memberRepository.save(MemberEntity.create("tester", "tester@example.com"));
        }
    }
}
