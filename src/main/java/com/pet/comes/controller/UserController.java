package com.pet.comes.controller;

import com.pet.comes.dto.Join.UserJoinDto;
import com.pet.comes.model.Entity.User;
import com.pet.comes.repository.UserRepository;
import com.pet.comes.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserService userService, UserRepository userRepository) {

        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("signup")
    public ResponseEntity signUp(@RequestBody UserJoinDto userJoinDto) {
        return userService.signUp(userJoinDto);
    }

    @GetMapping("account/{id}")
    public ResponseEntity myAccount(@PathVariable Long id){
        return userService.myAccount(id);
    }



}
