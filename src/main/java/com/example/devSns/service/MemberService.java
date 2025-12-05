package com.example.devSns.service;

import com.example.devSns.dto.CommentResponse;
import com.example.devSns.dto.LoginRequest;
import com.example.devSns.dto.PostResponse;
import com.example.devSns.dto.SignUpRequest;
import com.example.devSns.entity.Like;
import com.example.devSns.entity.Member;
import com.example.devSns.jwt.JwtUtil;
import com.example.devSns.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository MemberRepository;
    private final BCryptPasswordEncoder PasswordEncoder;
    private final JwtUtil JwtUtil;

    @Transactional
    public Member join(SignUpRequest signUpRequest) {
        validateDuplicateMember(signUpRequest.getUsername(), signUpRequest.getEmail()); // 이메일 중복 체크
        String encodedPassword = PasswordEncoder.encode(signUpRequest.getPassword());
        Member member = Member.create(
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encodedPassword
        );
        return MemberRepository.save(member);
    }


    private void validateDuplicateMember(String username, String email) {
        boolean usernameExists = MemberRepository.FindByUsername(username).isPresent();
        boolean emailExists = MemberRepository.FindByEmail(email).isPresent();

        if (usernameExists) {
            throw new IllegalStateException("Already exists member with username " + username);
        }

        if (emailExists) {
            throw new IllegalStateException("Already exists member with email " + email);
        }
    }
    public Member findMemberById(long id) {
        return MemberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("member not found"));
    }

    public List<Member> searchMembers(String keyword){
        return MemberRepository.FindByUsernameContaining(keyword);
    }

    public List<PostResponse> getPostsByMember(Long memberId){
        Member member = MemberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("member not found"));

        return member.getPosts().stream()
                .map(PostResponse::new)
                .toList();
    }

    public List<CommentResponse> getCommentsByMember(Long memberId) {
        Member member = MemberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("member not found"));

        return member.getComments().stream()
                .map(CommentResponse::new)
                .toList();
    }
    public List<PostResponse> getLikedPosts(Long memberId) {
        Member member = MemberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("member not found"));

        return member.getLikes().stream()
                .map(like -> new PostResponse(like.getPost()))
                .toList();
    }
    public boolean isEmailExists(String email) {
        return MemberRepository.FindByEmail(email).isPresent();
    }

    @Transactional(readOnly = false)
    public Member register(SignUpRequest signUpRequest) {
        validateDuplicateMember(signUpRequest.getUsername(), signUpRequest.getEmail());
        String encodedPassword = PasswordEncoder.encode(signUpRequest.getPassword());
        Member member = Member.create(signUpRequest.getUsername(), signUpRequest.getEmail(), encodedPassword);
        return MemberRepository.save(member);
    }
    @Transactional(readOnly = true)
    public List<Like> getLikesByMember(Long memberId) {
        Member member = MemberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("member not found"));

        return member.getLikes();
    }

    @Transactional(readOnly = true)
    public String login(LoginRequest loginRequest) {
        Member member = MemberRepository.FindByEmail(loginRequest.getEmail())
                .orElseThrow(()-> new RuntimeException("Invalid email or password"));

        boolean passwordMatch = PasswordEncoder.matches(loginRequest.getPassword(), member.getPassword());
        System.out.println("Password Match: " + passwordMatch);
        if (!passwordMatch) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = JwtUtil.generateToken(member.getId(), member.getEmail());
        System.out.println("Generated Token: " + token);  // 토큰 출력하여 확인
        return token;
    }


}

