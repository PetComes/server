package com.pet.comes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing // 시간 자동 변경이 가능하도록 함 : abstract class 인 TimeStaped 활용하기 위함
public class PetcomesApplication {

    public static void main(String[] args) {
        SpringApplication.run(PetcomesApplication.class, args);
    }

}
