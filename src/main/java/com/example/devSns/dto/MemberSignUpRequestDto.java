package com.example.devSns.dto;

import com.example.devSns.domain.Authority;
import com.example.devSns.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Setter
@NoArgsConstructor

public class MemberSignUpRequestDto {
    private String username;
    private String password;
    private String nickname;

    // DTO 정보를 바탕으로 Member 엔티티를 만드는 메서드
    public Member toMember(PasswordEncoder passwordEncoder) {
        return Member.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .nickname(nickname)
                .authority(Authority.ROLE_USER)
                .build();
    }

}