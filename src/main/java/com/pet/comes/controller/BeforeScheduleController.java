package com.pet.comes.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/schedule")
public class BeforeScheduleController {

    // private final ScheduleService scheduleService;
    //
    // @PostMapping
    // public ResponseEntity<String> registerSchedule(@RequestBody Map<String, String> scheduleMap) {
    //     return scheduleService.registerSchedule(scheduleMap);
    // }
    //
    // @PatchMapping
    // public ResponseEntity<String> modifySchedule(@RequestBody Map<String, String> scheduleMap) {
    //     return scheduleService.modifySchedule(scheduleMap);
    // }
    //
    // @DeleteMapping
    // public ResponseEntity<String> deleteSchedule(@RequestBody ScheduleReqDto scheduleDto) {
    //     return scheduleService.deleteSchedule(scheduleDto);
    // }
    //
    // @GetMapping
    // public ResponseEntity<String> getSchedule(@RequestBody ScheduleListDto scheduleListDto) {
    //     return scheduleService.getSchedule(scheduleListDto);
    // }
}
