package com.pet.comes.controller;

import java.util.*;

import javax.servlet.http.HttpServletRequest;


import com.pet.comes.config.securiy.JwtTokenProvider;
import com.pet.comes.config.component.CommonEncoder;
import com.pet.comes.dto.Join.UserJoinDto;
import com.pet.comes.dto.Rep.SignResultRepDto;
import com.pet.comes.dto.Req.RefreshTokenReqDto;
import com.pet.comes.dto.Req.SignInReqDto;
import com.pet.comes.model.Entity.User;
import com.pet.comes.response.DataResponse;
import com.pet.comes.response.NoDataResponse;
import com.pet.comes.response.ResponseMessage;
import com.pet.comes.response.Status;
import com.pet.comes.service.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/test/")
public class SignController {

    private final CustomUserDetailService customUserDetailService;

    private final CommonEncoder passwordEncoder = new CommonEncoder();

    private final JwtTokenProvider jwtTokenProvider;
    private final Status status;
    private final ResponseMessage message;

//    @Autowired
//    public SignController(ResponseMessage message, Status status, CustomUserDetailService customUserDetailService, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
//        this.customUserDetailService = customUserDetailService;
//        this.passwordEncoder = passwordEncoder;
//        this.jwtTokenProvider = jwtTokenProvider;
//        this.message = message;
//        this.status = status;
//    }

    /* U10 : 일반로그인 API --Tony*/
    @PostMapping("signin")
    @ResponseBody
    public SignResultRepDto signInUser(HttpServletRequest request, @RequestBody SignInReqDto signReqDto) {
        User user = (User) customUserDetailService.findByEmail(signReqDto.getEmail());
        SignResultRepDto signResultRepDto = new SignResultRepDto();

        try {
            if (passwordEncoder.matches(signReqDto.getPassword(), user.getPassword())) {
                System.out.println("비밀번호 일치");
                List<String> roleList = Arrays.asList(user.getRoles().split(","));
                signResultRepDto.setResult("success");
                signResultRepDto.setAccessToken(jwtTokenProvider.createToken(user.getEmail(), roleList)); // access token 만들기
                String tmpRefreshToken = jwtTokenProvider.createRefreshToken(user.getEmail());
                signResultRepDto.setRefreshToken(tmpRefreshToken); // refresh token 만들기

                // 새롭게 DB에 refresh token 변경
                user.setRefreshToken(tmpRefreshToken);
                customUserDetailService.save(user);

                return signResultRepDto;
            } else {
                signResultRepDto.setResult("fail");
                signResultRepDto.setMessage(" ID or Password is invalid.");
                return signResultRepDto;
            }
        } catch (NullPointerException e) {
            signResultRepDto.setResult("fail");
            signResultRepDto.setMessage("NullPointerException");
            return signResultRepDto;
        }
    }

    /* U1 : 회원가입 API --Tony*/
    @PostMapping("signup")
    @ResponseBody
    public SignResultRepDto addUser(HttpServletRequest request, @RequestBody UserJoinDto userJoinDto) {
        User user = new User(userJoinDto);

        user.setRoles("USER"); // 역할(권한) 부여
        user.setPassword(passwordEncoder.encode(userJoinDto.getPassword())); // 패스워드 암호화
        SignResultRepDto signResultRepDto = new SignResultRepDto();
        int result = customUserDetailService.signUpUser(user);
        if (result == 1) {
            signResultRepDto.setResult("success");
            signResultRepDto.setMessage("회원 가입 성공 !");
            return signResultRepDto;
        } else if (result == -1) {
            signResultRepDto.setResult("fail");
            signResultRepDto.setMessage("이메일이 존재합니다. 비밀번호 찾기를 진행해주세요 !");
            return signResultRepDto;
        } else {
            signResultRepDto.setResult("fail");
            signResultRepDto.setMessage("Ask system admin");
            return signResultRepDto;
        }

    }

    /* U9 : 액세스 토큰 새롭게 받기 (refresh token) API --Tony */
    @PostMapping("reaccess")
    @ResponseBody
    public ResponseEntity refreshAccessToken(HttpServletRequest request, @RequestBody RefreshTokenReqDto refreshTokenReqDto) {

        HashMap<String, String> tokens = new HashMap<>();

        Optional<User> isExist = customUserDetailService.findById(refreshTokenReqDto.getUserId());
        if (!isExist.isPresent())
            return new ResponseEntity(NoDataResponse.response(status.INVALID_ID, message.INVALID_ACCOUNT), HttpStatus.OK);

        User user = isExist.get();
        String userRefreshToken = user.getRefreshToken();
        String reqToken = refreshTokenReqDto.getRefreshToken();
        if (userRefreshToken != null && userRefreshToken.equals(reqToken)) { // refreshToken 유효해야
            List<String> roleList = Arrays.asList(user.getRoles().split(","));
            tokens.put("accesstoken", jwtTokenProvider.createToken(user.getEmail(), roleList));
            userRefreshToken = jwtTokenProvider.createRefreshToken(user.getEmail());
            tokens.put("refreshtoken", userRefreshToken);
            user.setRefreshToken(userRefreshToken); // refreshToken 업데이트

            customUserDetailService.save(user); // 새롭게 refreshToken 업데이트 된 User DB에 업데이트
            return new ResponseEntity(DataResponse.response(status.SUCCESS,
                    "access token 재발급 " + message.SUCCESS, tokens), HttpStatus.OK);


        }

        return new ResponseEntity(NoDataResponse.response(status.EXPIRED_TOKEN, message.EXPIRED_TOKEN), HttpStatus.OK);
    }

}