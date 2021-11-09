package com.pet.comes.controller;

import com.pet.comes.dto.Join.UserJoinDto;
import com.pet.comes.dto.Req.DogReqDto;
import com.pet.comes.repository.DogRepository;
import com.pet.comes.service.DogService;
import com.pet.comes.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dog")
public class DogController {


    private final DogService dogService;

    @Autowired
    public DogController(DogService dogService) {
        this.dogService = dogService;
    }

    @PostMapping("")
    public ResponseEntity addDog(@RequestBody DogReqDto dogReqDto) {
        return dogService.addDog(dogReqDto);
    }

}
