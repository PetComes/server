package com.pet.comes.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pet.comes.dto.Req.ScheduleDto;
import com.pet.comes.service.ScheduleService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<String> registerSchedule(@RequestBody Map<String, String> scheduleMap) {
        return scheduleService.registerSchedule(scheduleMap);
    }

    @PatchMapping
    public ResponseEntity<String> modifySchedule(@RequestBody Map<String, String> scheduleMap) {
        return scheduleService.modifySchedule(scheduleMap);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteSchedule(@RequestBody ScheduleDto scheduleDto) {
        return scheduleService.deleteSchedule(scheduleDto);
    }
}
