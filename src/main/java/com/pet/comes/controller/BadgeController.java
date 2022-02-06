package com.pet.comes.controller;

import com.pet.comes.dto.Req.AchievedBadgeReqDto;
import com.pet.comes.service.BadgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/badge")
public class BadgeController {

    private final BadgeService badgeService;

    @GetMapping("/{userId}")
    public ResponseEntity getAllAchievedBadge(@PathVariable String userId) {
        return badgeService.getAllAchievedBadge(userId);
    }
}
