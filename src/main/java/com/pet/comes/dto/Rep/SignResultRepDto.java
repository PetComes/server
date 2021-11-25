package com.pet.comes.dto.Rep;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;


// 로그인이 성공하면 토큰을 담아 보내거나 , 실패하면 메세지를 담아 보내기 위한 클래스
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class SignResultRepDto {

    private String result;
    private String message;
    private String token;


}
