package com.pet.comes.controller;

import com.pet.comes.service.SocialService;
import com.pet.comes.util.social.SocialLogInType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/social")
public class SocialController {

    @Autowired
    private SocialService socialService;

    @GetMapping
    public void getRedirectURL(@RequestParam("socialType") SocialLogInType socialLogInType) {
        socialService.requestSocialServer(socialLogInType);
    }
}
