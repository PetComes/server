package com.pet.comes.controller;

import com.pet.comes.dto.Req.AnimalRegistrationReqDto;
import com.pet.comes.dto.Req.DogBodyInformationDto;
import com.pet.comes.dto.Req.DogReqDto;
import com.pet.comes.service.DogService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dog")
public class DogController {

    private final DogService dogService;

//    @Autowired
//    public DogController(DogService dogService) {
//        this.dogService = dogService;
//    }

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

    /* A1 : 동물등록번호 등록 - Heather */
    @PatchMapping("/registration")
    public ResponseEntity registerAnimalRegistrationNo(AnimalRegistrationReqDto animalRegistrationReqDto) {
        return dogService.registerAnimalRegistrationNo(animalRegistrationReqDto);
    }

    /* A2 : 강아지 키, 몸무게 등록 및 수정 - Heather */
    @PatchMapping
    public ResponseEntity registerDogBodyInformation(DogBodyInformationDto dogBodyInformationDto) {
        return dogService.registerDogBodyInformation(dogBodyInformationDto);
    }
}
