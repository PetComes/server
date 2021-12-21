package com.pet.comes.dto.Req;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RefreshTokenReqDto {

        private Long userId;
        private String refreshToken;
}