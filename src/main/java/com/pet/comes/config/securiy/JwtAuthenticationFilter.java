package com.pet.comes.config.securiy;


import com.pet.comes.config.securiy.component.CommonEncoder;
import com.pet.comes.service.CustomUserDetailService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


// Jwt 가 유효한 토큰인지 인증하기 위한 filter
public class JwtAuthenticationFilter extends GenericFilterBean {

    private JwtTokenProvider jwtTokenProvider;
    private CustomUserDetailService customUserDetailService;

    // Jwt Provier 주입
    public JwtAuthenticationFilter(CustomUserDetailService customUserDetailService,JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.customUserDetailService = customUserDetailService;
    }

    // Request로 들어오는 Jwt Token의 유효성을 검증(jwtTokenProvider.validateToken)하는 filter를 filterChain에 등록합니다.
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
        System.out.println("----token get---- ");
        if (token != null && jwtTokenProvider.validateToken(token)) {
//            UserDetails userDetails = customUserDetailService.loadUserByUsername(token);
//            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
//                    userDetails, null,userDetails.getAuthorities());
            System.out.println("---valid token");
            Authentication auth = jwtTokenProvider.getAuthentication(token);

            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }
}