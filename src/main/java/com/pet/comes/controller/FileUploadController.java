package com.pet.comes.controller;


import com.pet.comes.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/s3/")
public class FileUploadController {

    private final FileUploadService fileUploadService;

    /* U11 : 프로필 이미지 업로드 API -- Tony*/
    @PostMapping("upload/img/profile/{userId}") // @RequestPart : Multipart의 Content-Type 헤더를 기억하도록 HttpMessageConverter로 전달된 멀티파트의 내용을 가질 수 있게 함.
    public ResponseEntity uploadProfileImage(@RequestPart MultipartFile file, @PathVariable Long userId) {
        return fileUploadService.uploadProfileImage(file,userId);
    }

    /* 다이어리 이미지 업로드 API -- Tony*/
    @PostMapping("upload/img/diary/{userId}") // @RequestPart : Multipart의 Content-Type 헤더를 기억하도록 HttpMessageConverter로 전달된 멀티파트의 내용을 가질 수 있게 함.
    public String uploadDiaryImage(@RequestPart MultipartFile file, @PathVariable Long userId) {
        return fileUploadService.uploadDiaryImage(file,userId);
    }
}
