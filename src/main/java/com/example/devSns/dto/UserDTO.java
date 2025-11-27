package com.example.devSns.dto;

import com.example.devSns.entities.Users;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.time.LocalDate;

public record UserDTO (
    String username,
    String loginID,
    String password,
    Integer age,
    LocalDate birthDay
) {
    public static Users dtoToEntity(UserDTO userDTO) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        return Users.builder()
                .username(userDTO.username())
                .loginID(userDTO.loginID())
                .password(passwordEncoder.encode(userDTO.password()))
                .birthday(userDTO.birthDay())
                .age(userDTO.age())
                .build();
    }
}
