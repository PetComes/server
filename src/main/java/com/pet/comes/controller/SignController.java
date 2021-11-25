package com.pet.comes.controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


import com.pet.comes.config.securiy.JwtTokenProvider;
import com.pet.comes.dto.Rep.SignResultRepDto;
import com.pet.comes.dto.Req.SignReqDto;
import com.pet.comes.model.Entity.User;
import com.pet.comes.service.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/test/")
public class SignController {

    private final CustomUserDetailService customUserDetailService;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public SignController(CustomUserDetailService customUserDetailService, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.customUserDetailService = customUserDetailService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider  = jwtTokenProvider;
    }

    // signin, login
    @PostMapping( "signin")
    @ResponseBody
    public SignResultRepDto signInUser(HttpServletRequest request, @RequestBody SignReqDto signReqDto) {
        User result = (User)customUserDetailService.findByEmail(signReqDto.getEmail());
        SignResultRepDto signResultRepDto = new SignResultRepDto();
       try{
        if (!passwordEncoder.matches(signReqDto.getPassword(), result.getPassword())) {
            signResultRepDto.setResult("fail");
            signResultRepDto.setMessage("ID or Password is invalid.");
            return signResultRepDto;
        }}catch (NullPointerException e){
           signResultRepDto.setResult("fail");
           signResultRepDto.setMessage("ID or Password is invalid.");
           return signResultRepDto;
       }
        List<String> roleList = Arrays.asList(result.getRoles().split(","));
        signResultRepDto.setResult("success");
        signResultRepDto.setToken(jwtTokenProvider.createToken(result.getEmail(), roleList)); // 좀 더 알아보기
        return signResultRepDto;
    }

    // signup,
    @PostMapping( "signup")
    @ResponseBody
    public SignResultRepDto addUser(HttpServletRequest request, @RequestBody SignReqDto signReqDto) {
        User user = new User(signReqDto);

        user.setRoles("ROLE_USER"); // 역할(권한) 부여
        user.setPassword(passwordEncoder.encode(signReqDto.getPassword())); // 패스워드 암호화
        SignResultRepDto signResultRepDto = new SignResultRepDto();
        int result = customUserDetailService.signUpUser(user);
        if (result == 1) {
            signResultRepDto.setResult("success");
            signResultRepDto.setMessage("SignUp complete");
            return signResultRepDto;
        } else if (result == -1) {
            signResultRepDto.setResult("fail");
            signResultRepDto.setMessage("There is the same name, please change your name.");
            return signResultRepDto;
        } else {
            signResultRepDto.setResult("fail");
            signResultRepDto.setMessage("Ask system admin");
            return signResultRepDto;
        }
    }
}