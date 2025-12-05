package com.example.devSns.web;

import com.example.devSns.config.JwtTokenProvider;
import com.example.devSns.domain.Member;
import com.example.devSns.domain.MemberRepository;
import com.example.devSns.domain.RefreshToken;
import com.example.devSns.service.MemberService;
import com.example.devSns.service.RefreshTokenService;
import com.example.devSns.web.dto.LoginRequest;
import com.example.devSns.web.dto.LoginResponse;
import com.example.devSns.web.dto.MemberCreateRequest;
import com.example.devSns.web.dto.MemberResponse;
import com.example.devSns.web.dto.TokenRefreshRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;

    public AuthController(MemberService memberService,
                          MemberRepository memberRepository,
                          JwtTokenProvider jwtTokenProvider,
                          PasswordEncoder passwordEncoder,
                          RefreshTokenService refreshTokenService) {
        this.memberService = memberService;
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenService = refreshTokenService;
    }

    /** 회원가입 */
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public MemberResponse signup(@Valid @RequestBody MemberCreateRequest req) {
        Member member = memberService.create(
                req.username(),
                req.password(),
                req.nickname(),
                req.bio()
        );
        return MemberResponse.from(member);
    }

    /** 로그인 + AccessToken / RefreshToken 발급 */
    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest req) {
        Member member = memberRepository.findByUsername(req.username())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "아이디 혹은 비밀번호가 올바르지 않습니다."));

        if (!passwordEncoder.matches(req.password(), member.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "아이디 혹은 비밀번호가 올바르지 않습니다.");
        }

        String accessToken = jwtTokenProvider.createToken(member.getUsername(), member.getId());
        RefreshToken refreshToken = refreshTokenService.create(member);

        return LoginResponse.of(accessToken, refreshToken.getToken(), member);
    }

    /** RefreshToken 으로 AccessToken 재발급 */
    @PostMapping("/refresh")
    public LoginResponse refresh(@Valid @RequestBody TokenRefreshRequest req) {
        // 1) 리프레시 토큰 검증
        RefreshToken refreshToken = refreshTokenService.validate(req.refreshToken());
        Member member = refreshToken.getMember();

        // 2) 새 AccessToken 발급
        String newAccessToken = jwtTokenProvider.createToken(member.getUsername(), member.getId());

        // 3) RefreshToken은 여기서는 재사용 (원하면 회전 로직 추가 가능)
        return LoginResponse.of(newAccessToken, refreshToken.getToken(), member);
    }
}
