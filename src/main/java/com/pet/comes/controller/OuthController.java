package com.pet.comes.controller;


import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/auth")
public class OuthController {

    /* 네이버 소셜로그인 API 테스트용 */
    @GetMapping("/naver")
    public String authNaver(@RequestParam String code,@RequestParam String state){
        return "code : " + code+" state : " + state ;
    }

    /* 카카오 소셜로그인 API 테스트용 */
    @GetMapping("/kakao")
    public String authKakao(@RequestParam String code,@RequestParam String state){
        return "code : " + code+" state : " + state ;
    }
}
