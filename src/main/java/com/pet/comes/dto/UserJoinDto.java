package com.pet.comes.dto;

import com.pet.comes.model.Status.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserJoinDto {
    private UserStatus status;
    private String name;
    private String email;
    private String password;
    private String nickname;
    private String introduction;
    private String imageUrl;
}
