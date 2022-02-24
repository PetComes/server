package com.pet.comes.repository;

import java.time.LocalDateTime;

public interface DiaryUserTestDto {
    Long getId();

    String getText();

    String getImageUrl();

    int getIspublic();

    int getHowManyComments();

    int getHowManyPins();

    LocalDateTime getRegisteredAt();

    UserInfo getUser();

    AddressInfo getAddress();

    interface UserInfo {
        Long getId();

        String getNickname();

        String getImageUrl();
    }

    interface AddressInfo {
        String getLocationName();
    }
}
