package com.khong.samuel.Mastermind_Reach.feature.menu;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class MenuRouter {
    @GetMapping("/")
    public String getMenu() {
        return "menu";
    }

}
