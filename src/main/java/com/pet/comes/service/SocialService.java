package com.pet.comes.service;

import com.pet.comes.util.social.NaverLogIn;
import com.pet.comes.util.social.SocialLogIn;
import com.pet.comes.util.social.SocialLogInType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SocialService {

    private final List<SocialLogIn> socialLogInList;
    private final HttpSession httpSession;
    private final HttpServletResponse response;

    public void requestSocialServer(SocialLogInType socialLogInType) {
        SocialLogIn socialLogIn = findSocialPlatform(socialLogInType);
        String redirectURL = socialLogIn.getRedirectURL();
        httpSession.setAttribute("state", socialLogIn.getState());
        try {
            response.sendRedirect(redirectURL);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String requestAccessToken(SocialLogInType socialLogInType, String state, String code) {
        String storedState = (String) httpSession.getAttribute("state");
        if(state.equals(storedState)) {
            SocialLogIn socialLogIn = findSocialPlatform(socialLogInType);
            return socialLogIn.requestAccessToken(state, code);
        }
        else {
            return "FAILED : State IS NOT SAME";
        }
    }

    public SocialLogIn findSocialPlatform(SocialLogInType socialLogInType) {
        return socialLogInList.stream()
                .filter(s -> s.getType() == socialLogInType)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("알 수 없는 socialType 입니다."));
    }
}
