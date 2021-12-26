package com.pet.comes.util.social;

public interface SocialLogIn {

    String getRedirectURL();
    String requestAccessToken();

    default SocialLogInType getType() {
        if(this instanceof NaverLogIn) {
            return SocialLogInType.N;
        }
        else {
            return SocialLogInType.FALSE;
        }
    }
}
