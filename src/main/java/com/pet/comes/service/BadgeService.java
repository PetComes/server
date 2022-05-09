package com.pet.comes.service;

import com.pet.comes.dto.Req.GetTheBadgeReqDto;
import com.pet.comes.model.Entity.*;
import com.pet.comes.model.EnumType.BadgeStatus;
import com.pet.comes.repository.*;
import com.pet.comes.repository.schedule.ScheduleRepository;
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
    private final PinRepository pinRepository;
    private final CommentRepository commentRepository;
    private final ScheduleRepository scheduleRepository;

    private final int ICON_ID_OF_GROOMING = 1;

    /**
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

    /**
     * B2 : 배지 획득조건 달성 확인하기
     */
    public ResponseEntity getTheBadge(GetTheBadgeReqDto badgeReqDto) {

        Long userId = badgeReqDto.getUserId();
        Long badgeId = badgeReqDto.getBadgeId();

        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()) {
            return new ResponseEntity(NoDataResponse.response(status.INVALID_ID, message.INVALID_USER), HttpStatus.OK);
        }

        if(badgeId == 1) { /* 신인상 : 가입 후 3일 이내 다이어리 작성 */
            LocalDateTime start = user.get().getCreatedAt();
            LocalDateTime end = start.plusDays(3);
            List<Diary> diaryList = diaryRepository.findByUserIdAndRegisteredAtBetween(userId, start, end);
            if(diaryList.size() == 0) {
                return new ResponseEntity(NoDataResponse.response(status.NOT_ACHIEVED, badgeId + "번 배지 조건 미달"), HttpStatus.OK);
            }

            boolean canActivate = false;
            for(Diary diary : diaryList) {
                if(diary.getIsDeleted() == 0) { // 하나라도 있으면 조건 달성
                    canActivate = true;
                    break;
                }
            }
            if(canActivate) { // 3일 내 작성한 다이어리가 하나라도 있으면 조건 달성
                return giveTheBadge(userId, badgeId);
            }
            else { // 조건 미달 -> 배지가 활성화되어 있으면 배지 회수, 아니면 조건 미달 응답
                return takeTheBadgeIfActivated(userId, badgeId);
            }
        }
        else if(badgeId == 2) { /* 모범견주 : 강아지등록번호 등록 */
            // 강아지 조회
            List<Dog> dogList = user.get().getFamily().getDogs();
            if(dogList.size() == 0) {
                return new ResponseEntity(NoDataResponse.response(status.INVALID_DOGID, badgeId + "번 배지 조건 미달 : 반려견 미등록"), HttpStatus.OK);
            }

            boolean isRegistered = false;
            for(Dog dog : dogList) {
                if(dog.getRegisterationNo() != null) { // 강아지등록번호 등록여부 확인
                    isRegistered = true;
                    break;
                }
            }
            if(isRegistered) { // 조건 달성
                return giveTheBadge(userId, badgeId);
            }
            else { // 조건 미달
                return takeTheBadgeIfActivated(userId, badgeId);
            }
        }
        else if(badgeId == 3) { /* 다둥이 부모 : 반려견 3마리 이상 등록 */
            // 강아지 조회
            List<Dog> dogList = user.get().getFamily().getDogs();
            if(dogList.size() == 0) {
                return new ResponseEntity(NoDataResponse.response(status.INVALID_DOGID, badgeId + "번 배지 조건 미달 : 반려견 미등록"), HttpStatus.OK);
            }
            else if(dogList.size() >=3 ) {
                return giveTheBadge(userId, badgeId);
            }
            else {
                return takeTheBadgeIfActivated(userId, badgeId);
            }
        }
        else if(badgeId == 4) { /* 대가족 : 4인 이상 가족 등록 */
            Family family = user.get().getFamily();
            List<User> userList = userRepository.findAllByFamily(family);
            if(userList.size() == 0) {
                return new ResponseEntity(NoDataResponse.response(status.FAMILY_UNREGISTERED, badgeId + "번 배지 조건 미달 : 가족 미등록"), HttpStatus.OK);
            }
            else if(userList.size() >= 4) {
                return giveTheBadge(userId, badgeId);
            }
            else {
                return takeTheBadgeIfActivated(userId, badgeId);
            }
        }
        else if(badgeId == 5) { /* 꿀팁콜렉터 : 핀한 게시글 총 50개 이상 */
            List<Pin> pinList = user.get().getPins();
            if(pinList.size() == 0) {
                return new ResponseEntity(NoDataResponse.response(status.PIN_UNREGISTERED, badgeId + "번 배지 조건 미달 : 핀한 게시글 없음"), HttpStatus.OK);
            }
            else if(pinList.size() >= 50) {
                return giveTheBadge(userId, badgeId);
            }
            else {
                return takeTheBadgeIfActivated(userId, badgeId);
            }
        }
        else if(badgeId == 6) { /* 고마운 분 : 1:1 문의를 많이 주시는 고마운 분 */
            return new ResponseEntity(NoDataResponse.response(status.BADGE_ACHIEVEMENT_CONDITION_UNREGISTERED, badgeId + "번 배지 조건 미등록 : ERROR"), HttpStatus.OK);
        }
        else if(badgeId == 7) { /* 베스트 셀러 : 등록 시점부터 일주일동안 핀을 100개 이상 받은 다이어리가 하나 이상 */
            List<Diary> diaryList = diaryRepository.findByUserId(userId);
            if(diaryList.size() == 0) {
                return new ResponseEntity(NoDataResponse.response(status.DIARY_UNREGISTERED, badgeId + "번 배지 조건 미달 : 다이어리 없음"), HttpStatus.OK);
            }

            LocalDateTime dateCreated, standard;
            int numOfPin;
            boolean doesMeetTheCondition = false;
            for(Diary diary : diaryList) {
                dateCreated = diary.getRegisteredAt();
                standard = dateCreated.plusDays(7);
                numOfPin = pinRepository.countPinByDiaryIdAndPinedAtBetween(diary.getId(), dateCreated, standard);
                if(numOfPin >= 100) {
                    doesMeetTheCondition = true;
                    break;
                }
            }
            if(doesMeetTheCondition) {
                return giveTheBadge(userId, badgeId);
            }
            else {
                return takeTheBadgeIfActivated(userId, badgeId);
            }
        }
        else if(badgeId == 8) { /* 나는야인싸 : 공개 다이어리 작성 최근 일주일동안 5개 이상 */
            LocalDateTime start = LocalDateTime.now();
            LocalDateTime end = start.minusDays(7);
            List<Diary> diaryList = diaryRepository.findByUserIdAndRegisteredAtBetween(userId, end, start);
            if(diaryList.size() == 0) {
                return new ResponseEntity(NoDataResponse.response(status.DIARY_UNREGISTERED, badgeId + "번 배지 조건 미달 : 최근 일주일동안 작성된 다이어리 없음"), HttpStatus.OK);
            }
            int diaryCounter = 0;
            for(Diary diary : diaryList) {
                if(diaryCounter >= 5) { // 공개 다이어리가 너무 많은 경우 성능 저하를 막기 위해 기준치까지만 탐색하도록 함
                    break;
                }
                if(diary.getIsPublic() == 1) {
                    diaryCounter++;
                }
            }
            if(diaryCounter >= 5) {
                return giveTheBadge(userId, badgeId);
            }
            else {
                return takeTheBadgeIfActivated(userId, badgeId);
            }
        }
        else if(badgeId == 9) { /* 모험가 : 최근 한달 기준 다이어리 장소태그 5개이상 */
            LocalDateTime start = LocalDateTime.now();
            LocalDateTime end = start.minusMonths(1);
            List<Diary> diaryList = diaryRepository.findByUserIdAndRegisteredAtBetween(userId, end, start);
            if(diaryList.size() == 0) {
                return new ResponseEntity(NoDataResponse.response(status.DIARY_UNREGISTERED, badgeId + "번 배지 조건 미달 : 최근 한달동안 장소 태그한 다이어리 없음"), HttpStatus.OK);
            }
            int diaryCounter = 0;
            for(Diary diary : diaryList) {
                if(diaryCounter >= 5) { // 다이어리가 너무 많은 경우 성능 저하를 막기 위해 기준치까지만 탐색하도록 함
                    break;
                }
                if(diary.getAddress() != null) {
                    diaryCounter++;
                }
            }
            if(diaryCounter >= 5) {
                return giveTheBadge(userId, badgeId);
            }
            else {
                return takeTheBadgeIfActivated(userId, badgeId);
            }
        }
        else if(badgeId == 10) { /* 댓글천사 : 최근 일주일동안 댓글 100개 이상 */
            LocalDateTime start = LocalDateTime.now();
            LocalDateTime end = start.minusDays(7);
            List<Comment> commentList = commentRepository.findByUserIdAndCommentedAtBetween(userId, end, start); // 여기 생각대로 될지 테스트 필요!!!!
            if(commentList.size() == 0) {
                return new ResponseEntity(NoDataResponse.response(status.COMMENT_UNREGISTERED, badgeId + "번 배지 조건 미달 : 최근 일주일동안 댓글 없음"), HttpStatus.OK);
            }
            else if(commentList.size() >= 100) {
                return giveTheBadge(userId, badgeId);
            }
            else {
                return takeTheBadgeIfActivated(userId, badgeId);
            }
        }
        // schedule 때문에 잠시 주석처리
        // else if(badgeId == 11) { /* 비지도그 : 최근 한달동안 일정 5개 이상 등록 */
        //     LocalDateTime start = LocalDateTime.now();
        //     LocalDateTime end = start.plusMonths(1);
        //     List<Schedule> scheduleList = scheduleRepository.findByUserIdAndRegisteredAtBetween(userId, end, start);
        //     if(scheduleList.size() == 0) {
        //         return new ResponseEntity(NoDataResponse.response(status.SCHEDULE_UNREGISTERED, badgeId + "번 배지 조건 미달 : 최근 한달동안 등록된 일정 없음"), HttpStatus.OK);
        //     }
        //     else if(scheduleList.size() >= 5) {
        //         return giveTheBadge(userId, badgeId);
        //     }
        //     else {
        //         return takeTheBadgeIfActivated(userId, badgeId);
        //     }
        // }
        // else if(badgeId == 12) { /* 패셔니스타 : 최근 한달기준 2회이상 미용시 */
        //     LocalDateTime start = LocalDateTime.now();
        //     LocalDateTime end = start.plusMonths(1);
        //     List<Schedule> scheduleList = scheduleRepository.findByUserIdAndRegisteredAtBetween(userId, end, start);
        //     if(scheduleList.size() == 0) {
        //         return new ResponseEntity(NoDataResponse.response(status.SCHEDULE_UNREGISTERED, badgeId + "번 배지 조건 미달 : 최근 한달동안 등록된 일정 없음"), HttpStatus.OK);
        //     }
        //
        //     int grooming = 0;
        //     boolean canActivate = false;
        //     for(Schedule schedule : scheduleList) {
        //         if(grooming >= 2) {
        //             canActivate = true;
        //             break;
        //         }
        //         if(schedule.getIconId() == ICON_ID_OF_GROOMING) {
        //             grooming++;
        //         }
        //     }
        //     if(canActivate) {
        //         return giveTheBadge(userId, badgeId);
        //     }
        //     else {
        //         return takeTheBadgeIfActivated(userId, badgeId);
        //     }
        // }
        else { // 없는 배지를 조회하려고 하는 경우
            return new ResponseEntity(NoDataResponse.response(status.INVALID_BADGE, "유효하지 않은 badgeId입니다."), HttpStatus.OK);
        }
    }

    private ResponseEntity giveTheBadge(Long userId, Long badgeId) { // 배지 부여
        Optional<Badge> badge = badgeListRepository.findById(badgeId);
        if(badge.isEmpty()) {
            return new ResponseEntity(NoDataResponse.response(status.INVALID_BADGE, "유효하지 않은 badgeId 입니다."), HttpStatus.OK);
        }
        ActivatedBadge activatedBadge = new ActivatedBadge(userId, badge.get(), BadgeStatus.ACTIVATED, LocalDateTime.now());
        badgeRepository.save(activatedBadge);
        return new ResponseEntity(DataResponse.response(status.SUCCESS, badgeId + "번 배지 획득", badgeId), HttpStatus.OK);

    }

    private ResponseEntity takeTheBadgeIfActivated(Long userId, Long badgeId) { // 배지 회수
        Optional<ActivatedBadge> activatedBadge = badgeRepository.findAllByUserIdAndBadgeId(userId, badgeId);
        if(activatedBadge.isEmpty() || activatedBadge.get().getStatus() != BadgeStatus.CANCELED) {
            return new ResponseEntity(NoDataResponse.response(status.NOT_ACHIEVED, badgeId + "번 배지 조건 미달"), HttpStatus.OK);
        }
        ActivatedBadge needToModify = activatedBadge.get();
        needToModify.setStatus(BadgeStatus.CANCELED);
        badgeRepository.save(needToModify);
        return new ResponseEntity(NoDataResponse.response(status.TAKEN_BADGE, badgeId + "번 배지 : 조건 미달로 배지가 회수되었습니다."), HttpStatus.OK);
    }
}
