package com.pet.comes.service;

import com.pet.comes.repository.IconRepository;
import com.pet.comes.response.DataResponse;
import com.pet.comes.response.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IconService {

    private final IconRepository iconRepository;
    private final Status status;

    public ResponseEntity getIconItems(String iconId) {
        String a = "";

        return new ResponseEntity(DataResponse.response(status.SUCCESS, "항목 조회 성공", a), HttpStatus.OK);
    }
}
