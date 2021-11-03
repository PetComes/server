package com.pet.comes.controller;

import com.pet.comes.model.Entity.Dog;
import com.pet.comes.repository.DogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class DogController {

    private final DogRepository dogRepository;

    @GetMapping("diary/{id}")
    public Optional<Dog> dogDiary(@PathVariable Long id) {
        Optional<Dog> dog = dogRepository.findById(id);
        return dog;
    }

}
