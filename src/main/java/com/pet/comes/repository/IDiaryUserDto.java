package com.pet.comes.repository;

import io.swagger.models.auth.In;

import java.time.LocalDateTime;

public interface IDiaryUserDto {
    Long getId();

    String getText();

    String getImageUrl();

    int getIsPublic();

    int getHowManyComments();

    int getHowManyPins();

//    LocalDateTime getRegisteredAt();

//    UserInfo getUser();

    Long getUserId();

    String getNickName();

    String getUserImageUrl();

//    interface UserInfo {
//
//        Long getUserId();
//
//        String getNickName();
//
//        String getUserImageUrl();
//    }
}
