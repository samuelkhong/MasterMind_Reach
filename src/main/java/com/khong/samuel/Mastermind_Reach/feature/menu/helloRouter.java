package com.khong.samuel.Mastermind_Reach.feature.menu;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/")
public class helloRouter {
    @GetMapping("/")
    public String helloWorld(Model model) {
        model.addAttribute("message", "Hello, World!");
        return "hello";
    }

}
