package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    @GetMapping("/register")
    public String register() {
        return "register"; 
    }
    
    @GetMapping("/ServicePage")
    public String servicePage(String email,String userId) {
        return "ServicePage";
    }
    
    @GetMapping("/workItemServices")
    public String workItemServices(String email,String userId) {
        return "workItemServices";
    }
    
}

