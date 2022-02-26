package com.pet.comes.service;

import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.pet.comes.service.s3.AwsS3UploadService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageService {

	private final AwsS3UploadService awsS3UploadService;

	public String registerImageAndGetUrl(MultipartFile image) throws IOException {
		return awsS3UploadService.uploadFile(image.getInputStream(), new ObjectMetadata(),
			image.getName() + UUID.randomUUID());
	}
}
