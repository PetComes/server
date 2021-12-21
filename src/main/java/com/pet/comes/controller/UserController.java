package com.pet.comes.controller;

import com.pet.comes.dto.Join.UserJoinDto;
import com.pet.comes.model.Entity.User;
import com.pet.comes.repository.UserRepository;
import com.pet.comes.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {

        this.userService = userService;
    }

//    @PostMapping("signup")
//    public ResponseEntity signUp(@RequestBody UserJoinDto userJoinDto) {
//        return userService.signUp(userJoinDto);
//    }

    @GetMapping("account/{id}")
//    @PreAuthorize("hasRole('USER')")
    public ResponseEntity myAccount(@PathVariable Long id){
        return userService.myAccount(id);
    }

    @GetMapping("family/{id}")
    public ResponseEntity myFamily(@PathVariable Long id){
        return userService.myFamily(id);
    }

    @PostMapping("test/validation/{nickname}")
    public ResponseEntity validNickname(@PathVariable String nickname){
        return userService.validNickname(nickname);
    }


}
