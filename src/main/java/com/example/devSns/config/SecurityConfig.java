package com.example.devSns.config;

import com.example.devSns.security.JwtFilter;
import com.example.devSns.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CSRF 설정 Disable
                .csrf((csrf) -> csrf.disable())

                // 세션 사용 안 함 (STATELESS)
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 요청 권한 설정
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/api/auth/**", "/h2-console/**").permitAll() // 로그인, 회원가입은 누구나 접근 가능
                        .anyRequest().authenticated() // 그 외 모든 요청은 인증 필요
                )

                // H2 Console iframe 허용
                .headers((headers) -> headers.frameOptions(frameOptions -> frameOptions.disable()))

                // JwtFilter 를 UsernamePasswordAuthenticationFilter 앞에 등록
                .addFilterBefore(new JwtFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}