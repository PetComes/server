package com.pet.comes.controller.icon;

import com.pet.comes.service.IconService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/icon")
public class IconController {

    private final IconService iconService;

    @GetMapping("/{iconId}")
    public ResponseEntity getIconItems(@PathVariable String iconId) {
        return iconService.getIconItems(iconId);
    }
}
