package com.pet.comes.dto;

import com.pet.comes.model.Entity.Family;
import com.pet.comes.model.Entity.User;
import com.pet.comes.model.EnumType.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class MyAccountDto {
    private UserStatus status;
    private String name;
    private String email;
    private String password;
    private String nickname;
    private String introduction;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Long familyId;

    public MyAccountDto(User user){
        this.status = user.getStatus();
        this.name = user.getName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.nickname = user.getNickname();
        this.introduction = user.getIntroduction();
        this.imageUrl = user.getImageUrl();
        this.createdAt = user.getCreatedAt();
        this.modifiedAt = user.getModifiedAt();
        this.familyId = user.getFamily().getId();
//        this.family = user.getFamily(); //
    }
}
