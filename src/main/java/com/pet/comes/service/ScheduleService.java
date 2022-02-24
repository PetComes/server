package com.pet.comes.service;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.pet.comes.model.Entity.User;
import com.pet.comes.model.Entity.schedule.Feeding;
import com.pet.comes.repository.UserRepository;
import com.pet.comes.repository.schedule.FeedingRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final FeedingRepository feedingRepository;
    private final UserRepository userRepository;

    /* iconId */
    private final int FEEDING = 1;
    private final int SNACK = 2;
    private final int POTTY = 3;
    private final int DRUG = 4;
    private final int HOSPITAL = 5;
    private final int SALON = 6;
    private final int BATH = 7;
    private final int SLEEP = 8;
    private final int PLAYING = 9;
    private final int TRAINING = 10;
    private final int MENSTRUATION = 11;
    private final int WALK = 12;
    private final int ETC = 13;

    public String registerSchedule(Map<String, String> scheduleMap) {
        int iconId = Integer.parseInt(scheduleMap.get("iconId"));

        // userId로 user 찾기
        Optional<User> user = userRepository.findById(Long.parseLong(scheduleMap.get("userId")));
        if(user.isEmpty()) {
            return "유효하지 않은 userId 입니다. userId : " + scheduleMap.get("userId");
        }

        if(iconId == FEEDING) {
            if(!scheduleMap.get("dryOrWet").equals("DRY") && !scheduleMap.get("dryOrWet").equals("WET")) {
                return "잘못된 입력입니다. dryOrWet(DRY/WET) : " + scheduleMap.get("dryOrWet");
            }
            if(!scheduleMap.get("date").matches("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$")) {
                return "잘못된 형식입니다. date(YYYY-MM-DD) : " + scheduleMap.get("date");
            }
            if(!scheduleMap.get("time").matches("^\\d{2}:([0-5][0-9]):(00)$")) {
                return "잘못된 형식입니다. time(HH:MM:00) : " + scheduleMap.get("time");
            }
            Feeding feeding = new Feeding(scheduleMap, user.get());
            feedingRepository.save(feeding);

            return "schedule 등록 성공";
        }
        if(iconId == SNACK) {

        }
        if(iconId == POTTY) {

        }
        if(iconId == DRUG) {

        }
        if(iconId == HOSPITAL) {

        }
        if(iconId == SALON) {

        }
        if(iconId == BATH) {

        }
        if(iconId == SLEEP) {

        }
        if(iconId == PLAYING) {

        }
        if(iconId == TRAINING) {

        }
        if(iconId == MENSTRUATION) {

        }
        if(iconId == WALK) {

        }
        if(iconId == ETC) {

        }

        return "fail";
    }
}
