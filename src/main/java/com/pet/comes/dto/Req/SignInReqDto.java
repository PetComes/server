package com.pet.comes.dto.Req;

import com.pet.comes.model.EnumType.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;


@AllArgsConstructor
@Getter
public class SignInReqDto {

    private String email;
    private String password;
}
