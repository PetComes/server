package com.pet.comes.controller.social;

import com.pet.comes.service.SocialService;
import com.pet.comes.util.social.SocialLogInType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/social")
public class SocialController {

    @Autowired
    private SocialService socialService;

    @GetMapping("/{socialType}")
    public void getRedirectURL(@PathVariable("socialType") SocialLogInType socialLogInType) {
        socialService.requestSocialServer(socialLogInType);
    }

    @GetMapping("/{socialType}/callback")
    public String getAccessToken(@PathVariable("socialType") SocialLogInType socialLogInType,
                               @RequestParam("state") String state,
                               @RequestParam("code") String code) {
        return socialService.requestAccessToken(socialLogInType, state, code);
    }
}
