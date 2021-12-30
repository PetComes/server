package com.pet.comes.controller;


import com.pet.comes.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
public class FileUploadController {

    private final FileUploadService fileUploadService;
/////
    /* U11 : s3 파일 업로드 API -- Tony*/
    @PostMapping("/s3/upload")
    public String uploadImage(@RequestPart MultipartFile file) {
        return fileUploadService.uploadImage(file);
    }

}
