package com.pet.comes.controller;

import com.pet.comes.dto.UserJoinDto;
import com.pet.comes.model.Entity.Dog;
import com.pet.comes.repository.DogRepository;
import com.pet.comes.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("signup")
    public ResponseEntity signUp(@RequestBody UserJoinDto userJoinDto) {
        return userService.signUp(userJoinDto);
    }




}
