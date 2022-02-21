package com.pet.comes.controller;

import java.util.Map;

import com.pet.comes.response.NoDataResponse;
import com.pet.comes.response.Status;
import com.pet.comes.service.ScheduleService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final Status status;

    @PostMapping
    public ResponseEntity<Object> registerSchedule(@RequestBody Map<String, String> schedule) {
        return new ResponseEntity<>(NoDataResponse.response(status.SUCCESS, scheduleService.registerSchedule(schedule)),
            HttpStatus.OK);
    }
}
