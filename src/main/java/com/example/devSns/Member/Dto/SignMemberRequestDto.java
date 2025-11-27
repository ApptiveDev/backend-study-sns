package com.example.devSns.Member.Dto;

import com.example.devSns.Member.Gender;
import com.example.devSns.Member.Member;
import jakarta.validation.constraints.*;

public record SignMemberRequestDto(

        @NotBlank(message = "이름은 공백 또는 null 값은 허용하지 않습니다")
        String nickname,

        @NotBlank(message = "이메일은 공백 또는 null 값은 허용하지 않습니다")
        @Email(message ="올바른 이메일 양식이 아닙니다")
        String email,

        @NotBlank(message = "비밀번호는 공백 또는 null 값은 허용하지 않습니다")
        @Size(min = 8, max =16, message = "비밀번호는 8자 이상 16자 이하로 설정해주세요")
        @Pattern(
                regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]+$",
                message = "영어, 숫자, 특수문자를 각각 최소 1개 이상 포함해야 합니다"
        )
        String password,

        @NotNull(message = "성별은 공백 또는 null 값은 허용하지 않습니다")
        Gender gender,

        @NotNull(message = "나이는 공백 또는 null 값은 허용하지 않습니다")
        Integer age

) {
    public Member toEntity(){
        return new Member(
                this.email,
                this.nickname,
                this.password,
                this.gender,
                this.age
        );
    }
}
