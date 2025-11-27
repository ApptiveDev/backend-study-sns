package com.example.devSns.services;

import com.example.devSns.authorities.Role;
import com.example.devSns.dto.JwtDTO;
import com.example.devSns.dto.LoginDTO;
import com.example.devSns.dto.UserDTO;
import com.example.devSns.entities.Users;
import com.example.devSns.repositories.UserRepository;
import com.example.devSns.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public String signUp(UserDTO userDTO) {
        Users user = UserDTO.dtoToEntity(userDTO);
        userRepository.save(user);
        return "회원가입 성공!";
    }

    public JwtDTO login(LoginDTO loginDTO) {
        Users findUser = userRepository.findByLoginID(loginDTO.loginID())
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("잘못된 아이디 또는 비밀번호입니다."));
        if(!findUser.getPassword().equals(loginDTO.password()))
            throw new AuthenticationCredentialsNotFoundException("잘못된 아이디 또는 비밀번호입니다.");
        else {
            String jwt = jwtUtil.generateToken(findUser.getLoginID(), findUser.getUsername(), Role.ROLE_USER);
            JwtDTO jwtDTO = JwtDTO.builder()
                    .token(jwt)
                    .build();
            return jwtDTO;
        }
    }
}
