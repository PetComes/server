package com.pet.comes.repository;

public interface OrderByIdDescWrapper {
    Long getId();

    String getText();

    String getDiaryImgUrl();

    int getIsPublic();

    int getHowManyComments();

    int getHowManyPins();

    UserInfo getUser();

    AddressInfo getAddress();

    interface UserInfo {
        String getNickname();

        String getImageUrl();
    }

    interface AddressInfo {
        String getLocationName();
    }
}
