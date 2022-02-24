package com.pet.comes.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.pet.comes.model.Entity.User;
import com.pet.comes.model.Entity.schedule.Drug;
import com.pet.comes.model.Entity.schedule.Feeding;
import com.pet.comes.model.Entity.schedule.Potty;
import com.pet.comes.model.Entity.schedule.Snack;
import com.pet.comes.repository.UserRepository;
import com.pet.comes.repository.schedule.DrugRepository;
import com.pet.comes.repository.schedule.FeedingRepository;
import com.pet.comes.repository.schedule.PottyRepository;
import com.pet.comes.repository.schedule.SnackRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final FeedingRepository feedingRepository;
    private final SnackRepository snackRepository;
    private final PottyRepository pottyRepository;
    private final DrugRepository drugRepository;
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

        scheduleMap.putIfAbsent("date", LocalDate.now().toString().substring(0,10));
        scheduleMap.putIfAbsent("time", LocalTime.now().toString().substring(0,6) + "00");
        scheduleMap.putIfAbsent("memo", null);

        if(!scheduleMap.get("date").matches("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$")) {
            return "잘못된 형식입니다. date(YYYY-MM-DD) : " + scheduleMap.get("date");
        }
        if(scheduleMap.get("time").length() == 5) {
            scheduleMap.put("time", scheduleMap.get("time") + ":00");
        }
        if(!scheduleMap.get("time").matches("^\\d{2}:([0-5][0-9]):(00)$")) {
            return "잘못된 형식입니다. time(HH:MM:00) : " + scheduleMap.get("time");
        }

        if(iconId == FEEDING) {
            String dryOrWet = scheduleMap.get("dryOrWet");
            if(!dryOrWet.equals("DRY") && !dryOrWet.equals("WET")) {
                return "dryOrWet 값은 DRY 또는 WET 중 하나여야 합니다. dryOrWet : " + dryOrWet;
            }
            Feeding feeding = new Feeding(scheduleMap, user.get());
            feedingRepository.save(feeding);
            return "schedule 등록 성공";
        }
        if(iconId == SNACK) {
            String kind = scheduleMap.get("kind");
            if(!kind.contains("홈메이드 : ") && !kind.contains("구매 : ")) {
                return "kind 값은 \'홈메이드 : \' 또는 \'구매 : \' 로 시작해야 합니다. kind : " + kind;
            }
            Snack snack = new Snack(scheduleMap, user.get());
            snackRepository.save(snack);
            return "schedule 등록 성공";
        }
        if(iconId == POTTY) {
            String kind = scheduleMap.get("kind");
            if(!kind.equals("URINE") && !kind.equals("FECES")) {
                return "kind 값은 URINE(소변) 또는 FECES(대변) 중 하나여야 합니다. kind : " + kind;
            }
            Potty potty = new Potty(scheduleMap, user.get());
            pottyRepository.save(potty);
            return "schedule 등록 성공";
        }
        if(iconId == DRUG) {
            scheduleMap.putIfAbsent("kind", null);
            scheduleMap.putIfAbsent("prescriptionUrl", null);
            scheduleMap.putIfAbsent("expenses", "-1");

            Drug drug = new Drug(scheduleMap, user.get());
            drugRepository.save(drug);
            return "schedule 등록 성공";
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
