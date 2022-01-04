package com.pet.comes.controller;

import com.pet.comes.dto.Join.UserJoinDto;
import com.pet.comes.model.Entity.User;
import com.pet.comes.repository.UserRepository;
import com.pet.comes.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class UserController {

    private final UserService userService;

    //    @PostMapping("signup")
//    public ResponseEntity signUp(@RequestBody UserJoinDto userJoinDto) {
//        return userService.signUp(userJoinDto);
//    }

    /* H1 : 내 계정정보 조회 --Tony */
    @GetMapping("account/{id}")
//    @PreAuthorize("hasRole('USER')")
    public ResponseEntity myAccount(@PathVariable Long id) {
        return userService.myAccount(id);
    }

    /* H2 읽지 않은 알림 개수 조회 --Tony */
    @GetMapping("alarm/count/{userId}")
    public ResponseEntity alarmCount(@PathVariable Long userId) {
        return userService.alarmCount(userId);
    }

    /* H6 내 핀 목록 조회 API -- Tony */
    @GetMapping("list/pin/{userId}")
    public ResponseEntity pinList(@PathVariable Long userId) {
        return userService.pinList(userId);
    }

    /* H7 : 내 가족 목록 조회 --Tony */
    @GetMapping("family/{id}")
    public ResponseEntity myFamily(@PathVariable Long id) {
        return userService.myFamily(id);
    }

    /* U1 : 회원가입 및 로그인 --Tony */
    @PostMapping("test/validation/{nickname}")
    public ResponseEntity validNickname(@PathVariable String nickname) {
        return userService.validNickname(nickname);
    }

    /* S10 : 클릭한 계정 프로필 확인하기 --Tony */
    @GetMapping("profile/{userName}")
    public ResponseEntity getUserProfile(@PathVariable String userName) {
        return userService.getUserProfile(userName);
    }


}
