package com.pet.comes.config.securiy.component;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CommonEncoder implements PasswordEncoder {
    private final PasswordEncoder passwordEncoder;

    public CommonEncoder() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public CommonEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String encode(CharSequence rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
