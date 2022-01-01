package com.pet.comes.service;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.pet.comes.model.Entity.User;
import com.pet.comes.repository.UserRepository;
import com.pet.comes.response.DataResponse;
import com.pet.comes.response.NoDataResponse;
import com.pet.comes.response.ResponseMessage;
import com.pet.comes.response.Status;
import com.pet.comes.service.s3.UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class FileUploadService {

    private final UploadService s3Service;
    private final UserRepository userRepository;
    private final Status status;
    private final ResponseMessage message;

    /* 프로필 이미지 업로드 API --Tony*/
    public ResponseEntity uploadProfileImage(MultipartFile file, Long userId) {
        String fileName = createFileName(file.getOriginalFilename());
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());
        try (InputStream inputStream = file.getInputStream()) {
            s3Service.uploadFile(inputStream, objectMetadata, fileName);
        } catch (IOException e) {
            throw new IllegalArgumentException(String.format("파일 변환 중 에러가 발생하였습니다 (%s)", file.getOriginalFilename()));
        }
        String imageUrl = s3Service.getFileUrl(fileName);
        // DB에 해달 imageurl 저장하기
        Optional<User> isExist = userRepository.findById(userId);
        if(!isExist.isPresent())
            return new ResponseEntity(NoDataResponse.response(status.INVALID_ID, message.NOT_VALID_ACCOUNT ), HttpStatus.OK);

        User user = isExist.get();
        user.setImageUrl(imageUrl);
        userRepository.save(user);

        return new ResponseEntity(DataResponse.response(status.SUCCESS,
                "프로필사진 등록 " + message.SUCCESS, imageUrl), HttpStatus.OK);

    }

    /* 다이어리 이미지 업로드 API --Tony*/
    public String uploadDiaryImage(MultipartFile file,Long userId) {
        String fileName = createFileName(file.getOriginalFilename());
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());
        try (InputStream inputStream = file.getInputStream()) {
            s3Service.uploadFile(inputStream, objectMetadata, fileName);
        } catch (IOException e) {
            throw new IllegalArgumentException(String.format("파일 변환 중 에러가 발생하였습니다 (%s)", file.getOriginalFilename()));
        }
        String imageUrl = s3Service.getFileUrl(fileName);
        // DB에 해달 imageurl 저장하기



        return imageUrl;
    }
    private String createFileName(String originalFileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(originalFileName));
    }

    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new IllegalArgumentException(String.format("잘못된 형식의 파일 (%s) 입니다", fileName));
        }
    }

}