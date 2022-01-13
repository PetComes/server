package com.pet.comes.util.social;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.stream.Collectors;

public class NaverLogIn implements SocialLogIn {

    private String state = generateState();

    @Value("${social.naver.client.id}")
    private String NAVER_CLIENT_ID;

    @Value("${social.naver.client.secret}")
    private String NAVER_CLIENT_SECRET;

    @Value("${social.naver.callback}")
    private String NAVER_CALLBACK_URL;

    private String NAVER_AUTHENTICATION_URL = "https://nid.naver.com/oauth2.0/authorize?";
    private String NAVER_ISSUE_TOKEN_URL = "https://nid.naver.com/oauth2.0/token?";


    @Override
    public String getState() {
        return state;
    }

    @Override
    public String getRedirectURL() {
        HashMap<String, String> params = new HashMap<>();
        params.put("client_id", NAVER_CLIENT_ID);
        params.put("response_type", "code");
        params.put("redirect_uri", NAVER_CALLBACK_URL);
        params.put("state", state);

        String setParams = params.entrySet().stream()
                .map(x -> x.getKey() + "=" + x.getValue())
                .collect(Collectors.joining("&"));

        return NAVER_AUTHENTICATION_URL + setParams;
    }

    @Override
    public String requestAccessToken(String state, String code) {
        HashMap<String, String> params = new HashMap<>();
        params.put("client_id", NAVER_CLIENT_ID);
        params.put("client_secret", NAVER_CLIENT_SECRET);
        params.put("grant_type", "authorization_code");
        params.put("state", state);
        params.put("code", code);

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        HttpEntity<HashMap<String, String>> headerAndBody = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                NAVER_ISSUE_TOKEN_URL,
                HttpMethod.POST,
                headerAndBody,
                String.class
        );

        if(response.getStatusCode() == HttpStatus.OK){
            return response.getBody();
        }

        return "FAILED : AccessToken";
    }

    public String generateState() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }
}
