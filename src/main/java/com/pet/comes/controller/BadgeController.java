package com.pet.comes.controller;

import com.pet.comes.dto.Req.GetTheBadgeReqDto;
import com.pet.comes.service.BadgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/badge")
public class BadgeController {

    private final BadgeService badgeService;

    @GetMapping("/{userId}")
    public ResponseEntity getAllAchievedBadge(@PathVariable Long userId) {
        return badgeService.getAllAchievedBadge(userId);
    }

    @PostMapping
    public ResponseEntity getTheBadge(@RequestBody GetTheBadgeReqDto badgeReqDto) {
        return badgeService.getTheBadge(badgeReqDto);
    }
}
