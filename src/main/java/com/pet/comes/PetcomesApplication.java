package com.pet.comes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableJpaAuditing // JPA Auditing을 활성화 :: ex) 시간 자동 변경이 가능하도록 함 : abstract class 인 TimeStaped 활용하기 위함
public class PetcomesApplication {

    public static void main(String[] args) {
        SpringApplication.run(PetcomesApplication.class, args);
    }

    // 스프링은 PasswordEncoder를 bean 으노 인식하지 못하기 때문에 직접 등록
    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
