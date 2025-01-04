package com.khong.samuel.Mastermind_Reach.core.Authentication.controller;

import com.khong.samuel.Mastermind_Reach.core.Authentication.model.User;
import com.khong.samuel.Mastermind_Reach.core.Authentication.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class registrationController {

    private final PasswordEncoder passwordEncoder;

    private final UserService userService;

    registrationController(PasswordEncoder passwordEncoder, UserService userService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }


    @GetMapping("/register")
    String getRegister(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user")  User user, BindingResult result, Model model) {
        // run through error checks
        if (user.getUsername().isEmpty()) {
            result.rejectValue("username", "error.username", "Username is required");
        }
        if (user.getPassword().isEmpty()) {
            result.rejectValue("password", "error.password", "Password is required");
        }

        if (user.getEmail().isEmpty()) {
            result.rejectValue("email", "error.email", "Email is required");
        }


        // Validate the user input
        if (result.hasErrors()) {
            return "register";  // If there are errors, return to the form to fix them
        }

        // Save password as a hash
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Save the user
        userService.save(user);

        // After saving the user, redirect to the login page or home page
        return "redirect:/login";  // Redirect to login page after successful registration
    }
}
