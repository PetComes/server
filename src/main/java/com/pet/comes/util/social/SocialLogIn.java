package com.pet.comes.util.social;

public interface SocialLogIn {

    String getState();
    String getRedirectURL();
    String requestAccessToken(String state, String code);

    default SocialLogInType getType() {
        if(this instanceof NaverLogIn) {
            return SocialLogInType.N;
        }
        else {
            return SocialLogInType.FALSE;
        }
    }
}
