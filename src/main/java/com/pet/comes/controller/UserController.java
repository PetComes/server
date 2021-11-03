package com.pet.comes.controller;

import com.pet.comes.dto.Join.UserJoinDto;
import com.pet.comes.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("signup")
    public ResponseEntity signUp(@RequestBody UserJoinDto userJoinDto) {
        return userService.signUp(userJoinDto);
    }




}
