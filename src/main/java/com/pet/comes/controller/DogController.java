package com.pet.comes.controller;

import com.pet.comes.dto.Req.DogReqDto;
import com.pet.comes.service.DogService;
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

    /* H4 : 강아지 등록 API -- Tony*/
    @PostMapping("/{userId}")
    public ResponseEntity addDog(@PathVariable Long userId , @RequestBody DogReqDto dogReqDto) {
        return dogService.addDog(userId,dogReqDto);
    }

    /* H5 : 강아지별 프로필 조회 API -- Tony*/
    @GetMapping("/profile/{nickName}/{dogName}")
    public ResponseEntity getDogProfile(@PathVariable String nickName, @PathVariable String dogName){
        return dogService.getDogProfile(nickName,dogName);
    }

}
