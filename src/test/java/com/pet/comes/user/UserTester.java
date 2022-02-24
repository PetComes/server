package com.pet.comes.user;

import com.pet.comes.model.Entity.User;
import com.pet.comes.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class UserTester {

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName(" tester ")
    void userTest() {
//        User user = userRepository.findById(1L).get();
//                .orElseThrow(() -> new IllegalAccessError("*************error*************"));
//        Assertions.assertThat(user).isInstanceOf(User.class);
//        System.out.println(user);

    }
}
