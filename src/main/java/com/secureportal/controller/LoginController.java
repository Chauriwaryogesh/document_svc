package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String login() {
        return "login";  // This looks for login.html in src/main/resources/templates/
    }
    @GetMapping("/register")
    public String register() {
        return "register";  // This looks for login.html in src/main/resources/templates/
    }
    
}

