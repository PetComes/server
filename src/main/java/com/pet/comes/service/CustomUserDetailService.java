package com.pet.comes.service;

import com.pet.comes.repository.OuthRepository;
import com.pet.comes.model.Entity.User;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final OuthRepository outhRepository;

    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        return outhRepository.findByEmail(userEmail);
    }

    public UserDetails findByEmail(String email) {
        return outhRepository.findByEmail(email);
    }

    public int signUpUser(User user) {
        if (outhRepository.findByEmail(user.getEmail()) == null) {
            outhRepository.save(user);
            return 1; // 회원가입 성공시
        } else
            return -1; // 회원가입 실패시
    }

    public void deletUser(String email) { // 유저 삭제 ** 조금 위험함 사용할 때 다시 코드 수정하기
        outhRepository.deleteByEmail(email);

    }

    public Optional<User> findById(Long userId) {
        return outhRepository.findById(userId);
    }

    public void save(User user) {
        if (user != null)
            outhRepository.save(user);
    }
}

