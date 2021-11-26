package com.pet.comes.config.securiy;

// import 생략

import com.pet.comes.model.Entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/* 클라에서 api 호출을 하면 Controller에 도달하기 전에 filter를 거침
* 이러한 filter 설정을 아래에 한거임. : Token 기반 인증 절차 구현 */
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /*
    * Spring Security filter 설정 : HttpSecurity
    * URL에대한 보안걸기 */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable() // rest api 이므로 기본설정 사용안함. 기본설정은 비인증시 로그인폼 화면으로 리다이렉트 된다.
                .csrf().disable() // rest api이므로 csrf 보안이 필요없으므로 disable처리.
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // jwt token으로 인증하므로 세션은 필요없으므로 생성안함.
                .and()
                .authorizeRequests() // 다음 리퀘스트에 대한 사용권한 체크 , 여기서 부터 호출되는 메소드들의 요청 보안 수준을 설정정                .antMatchers("/test/signin", "/test/signup").permitAll() // 가입 및 인증 주소는 누구나 접근가능
//                    .antMatchers(HttpMethod.GET, "/helloworld/**").permitAll() // hellowworld로 시작하는 GET요청 리소스는 누구나 접근가능
                .antMatchers(HttpMethod.GET, "/**").hasRole("user") // 일단 모든 리소스 혀용
                .antMatchers(HttpMethod.POST,"/test/**").permitAll()
                .antMatchers(HttpMethod.PUT,"/**").permitAll()
                .antMatchers(HttpMethod.DELETE,"/**").permitAll()
                .anyRequest().hasRole("user") // 그외 나머지 요청은 모두 인증된 회원만 접근 가능
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class); // jwt token 필터를 id/password 인증 필터 전에 넣는다

    }

    @Override // ignore check swagger resource
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/v2/api-docs", "/swagger-resources/**",
                "/swagger-ui.html", "/webjars/**", "/swagger/**");

    }
}