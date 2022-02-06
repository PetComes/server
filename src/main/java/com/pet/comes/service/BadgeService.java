package com.pet.comes.service;

import com.pet.comes.model.Entity.ActivatedBadge;
import com.pet.comes.repository.BadgeRepository;
import com.pet.comes.response.DataResponse;
import com.pet.comes.response.NoDataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BadgeService {

    private final BadgeRepository badgeRepository;

    public ResponseEntity getAllAchievedBadge(String id) {
        Long userId = Long.parseLong(id);
        List<ActivatedBadge> activatedBadgeList = badgeRepository.findAllByUserId(userId);
        if(activatedBadgeList.size() == 0) {
            return new ResponseEntity(NoDataResponse.response(200, "획득한 배지가 없습니다."), HttpStatus.OK);
        }
        else {
            return new ResponseEntity(DataResponse.response(200, "활성 배지 조회 성공", activatedBadgeList), HttpStatus.OK);
        }
    }
}
