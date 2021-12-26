package com.pet.comes.util.social;

public class NaverLogIn implements SocialLogIn {

    private String NAVER_CLIENT_ID;
    private String NAVER_CLIENT_SECRET;
    private String NAVER_CALLBACK_URL;


    @Override
    public String getRedirectURL() {
        return null;
    }

    @Override
    public String requestAccessToken() {
        return null;
    }

}
