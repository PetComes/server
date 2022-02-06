package com.pet.comes.service;

import com.pet.comes.dto.Req.GetTheBadgeReqDto;
import com.pet.comes.model.Entity.*;
import com.pet.comes.model.EnumType.BadgeStatus;
import com.pet.comes.repository.BadgeListRepository;
import com.pet.comes.repository.BadgeRepository;
import com.pet.comes.repository.DiaryRepository;
import com.pet.comes.repository.UserRepository;
import com.pet.comes.response.DataResponse;
import com.pet.comes.response.NoDataResponse;
import com.pet.comes.response.ResponseMessage;
import com.pet.comes.response.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BadgeService {

    private final Status status;
    private final ResponseMessage message;
    private final BadgeRepository badgeRepository;
    private final UserRepository userRepository;
    private final DiaryRepository diaryRepository;
    private final BadgeListRepository badgeListRepository;

    /*
     * B1 : 이전에 획득한 배지 전부 조회하기
     */
    public ResponseEntity getAllAchievedBadge(Long userId) {

        List<ActivatedBadge> activatedBadgeList = badgeRepository.findAllByUserId(userId);
        if(activatedBadgeList.size() == 0) {
            return new ResponseEntity(NoDataResponse.response(status.NOTHING, "획득한 배지가 없습니다."), HttpStatus.OK);
        }
        else {
            return new ResponseEntity(DataResponse.response(200, "활성 배지 조회 성공", activatedBadgeList), HttpStatus.OK);
        }
    }

    /*
     * B2 : 배지 획득조건 달성 확인하기
     */
    public ResponseEntity getTheBadge(GetTheBadgeReqDto badgeReqDto) {

        Long userId = badgeReqDto.getUserId();
        Long badgeId = badgeReqDto.getBadgeId();

        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()) {
            return new ResponseEntity(NoDataResponse.response(status.INVALID_ID, message.INVALID_USER), HttpStatus.OK);
        }

        if(badgeId == 1) { // 신인상 : 가입 후 3일 이내 다이어리 작성
            LocalDateTime start = user.get().getCreatedAt();
            LocalDateTime end = start.plusDays(3);
            List<Diary> diaryList = diaryRepository.findByUserIdAndRegisteredAtBetween(userId, start, end);
            if(diaryList.size() == 0) {
                return new ResponseEntity(NoDataResponse.response(status.NOT_ACHIEVED, badgeId + "번 배지 조건 미달"), HttpStatus.OK);
            }

            boolean canActivate = false;
            for(Diary diary : diaryList) {
                if(diary.getIsDeleted() == 0) { // 하나라도 있으면 가능
                    canActivate = true;
                    break;
                }
            }
            if(canActivate) { // 3일 내 작성한 다이어리가 하나라도 있으면 배지 활성화시키고, 있다고 응답
                Optional<Badge> badge = badgeListRepository.findById(badgeId);
                if(badge.isEmpty()) {
                    return new ResponseEntity(NoDataResponse.response(status.DB_NO_DATA, "DB 배지조회 에러"), HttpStatus.OK);
                }
                ActivatedBadge activatedBadge = new ActivatedBadge(userId, badge.get(), BadgeStatus.ACTIVATED, LocalDateTime.now());
                badgeRepository.save(activatedBadge);
                return new ResponseEntity(DataResponse.response(status.SUCCESS, badgeId + "번 배지 획득", badgeId), HttpStatus.OK);
            }
            else { // 조건 미달성
                return new ResponseEntity(NoDataResponse.response(status.NOT_ACHIEVED, badgeId + "번 배지 조건 미달"), HttpStatus.OK);
            }
        }
        else if(badgeId == 2) { // 모범견주 : 강아지등록번호 등록
            // 가족 id 조회, 강아지 조회
            List<Dog> dogList = user.get().getFamily().getDogs();
            if(dogList.size() == 0) {
                return new ResponseEntity(NoDataResponse.response(status.INVALID_DOGID, message.NO_DOG), HttpStatus.OK);
            }
            // 강아지등록번호 등록여부 확인
            boolean isRegistered = false;
            for(Dog dog : dogList) {
                if(dog.getRegisterationNo() != null) {
                    isRegistered = true;
                    break;
                }
            }
            if(isRegistered) { // 조건 달성
                return new ResponseEntity(DataResponse.response(status.SUCCESS, badgeId + "번 배지 획득", badgeId), HttpStatus.OK);
            }
            else { // 조건 미달성
                return new ResponseEntity(NoDataResponse.response(status.NOT_ACHIEVED, badgeId + "번 배지 조건 미달"), HttpStatus.OK);
            }
        }
        /*
        else if(badgeId == 3) {

        }
        */
        else { // 없는 배지를 조회하려고 하는 경우
            return new ResponseEntity(NoDataResponse.response(status.INVALID_ID, "유효하지 않은 badgeId입니다."), HttpStatus.OK);
        }
    }
}
