package com.khong.samuel.Mastermind_Reach.core.Authentication.controller;

import com.khong.samuel.Mastermind_Reach.core.Authentication.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class registrationController {

    @GetMapping("/register")
    String getRegister(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }
}
