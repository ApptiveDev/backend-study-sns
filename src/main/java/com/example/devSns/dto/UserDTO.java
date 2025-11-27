package com.example.devSns.dto;

import com.example.devSns.entities.Users;


import java.time.LocalDate;

public record UserDTO (
    String username,
    String loginID,
    String password,
    Integer age,
    LocalDate birthDay
) {
    public static Users dtoToEntity(UserDTO userDTO) {
        return Users.builder()
                .username(userDTO.username())
                .loginID(userDTO.loginID())
                .password(userDTO.password())
                .birthday(userDTO.birthDay())
                .age(userDTO.age())
                .build();
    }
}
