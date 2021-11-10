package com.pet.comes.dto.Join;

import com.pet.comes.model.EnumType.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

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
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

}
