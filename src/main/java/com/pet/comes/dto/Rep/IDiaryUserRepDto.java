package com.pet.comes.dto.Rep;

import java.time.LocalDateTime;

//[참고자료] : https://www.baeldung.com/jpa-queries-custom-result-with-aggregation-functions
public interface IDiaryUserRepDto {
    Long getId();

    String getText();

    String getLocationName();

    String getImageUrl();

    int getIsPublic();

    int getHowManyComments();

    int getHowManyPins();

    LocalDateTime getRegisteredAt();

    Long getUserId();

    String getNickName();

    String getUserImageUrl();

    String getDogName();

//    interface UserInfo {
//
//        Long getUserId();
//
//        String getNickName();
//
//        String getUserImageUrl();
//    }
}
