package com.pet.comes.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.pet.comes.repository.ScheduleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final int FEEDING = 1;
    private final int SNACK = 2;
    private final int POTTY = 3;
    private final int DRUG = 4;
    private final int HOSPITAL = 5;
    private final int SALON = 6;
    private final int BATH = 7;
    private final int PLAYING = 8;
    private final int TRAINING = 9;
    private final int MENSTRUATION = 10;
    private final int WALK = 11;
    private final int ETC = 99;

    public String registerSchedule(Map<String, String> scheduleParameters) {
        int iconId = Integer.parseInt(scheduleParameters.get("iconId"));

        // Schedule schedule  = new Schedule(scheduleParameters); // 공통항목들
        List<Addition> additionList;

        if(iconId == FEEDING) {

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
        if(iconId == WALK) {

        }
        if(iconId == ETC) {

        }

        // scheduleRepository.save(schedule);
        return "true";
    }
}
