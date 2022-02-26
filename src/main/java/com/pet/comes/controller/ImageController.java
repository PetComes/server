package com.pet.comes.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.pet.comes.response.DataResponse;
import com.pet.comes.response.ResponseMessage;
import com.pet.comes.response.Status;
import com.pet.comes.service.ImageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/image")
public class ImageController {

	private final ImageService imageService;
	private final Status status;
	private final ResponseMessage responseMessage;

	@PostMapping
	public ResponseEntity registerImageAndGetUrl(MultipartFile image) throws IOException {
		return new ResponseEntity(
			DataResponse.response(status.SUCCESS, responseMessage.SUCCESS, imageService.registerImageAndGetUrl(image)),
			HttpStatus.OK);
	}
}
