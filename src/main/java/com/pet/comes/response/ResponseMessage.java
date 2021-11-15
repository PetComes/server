package com.pet.comes.response;

import org.springframework.stereotype.Component;

@Component
public class ResponseMessage {
    /*
     * Set same variable name class Status's variable name
     */

    public String SUCCESS = "성공";
    public String DUPLICATED_EMAIL = "사용할 수 없는 이메일입니다.(중복)";
    public String NOT_ENTERED = "입력되지 않은 필수 항목이 있습니다.";
    public String DUPLICATED_NICKNAME = "사용할 수 없는 닉네임입니다.(중복)";
    public String NOT_VALID_ACCOUNT = "계정 정보를 불러올 수 없습니다."; // accountId validation

}
