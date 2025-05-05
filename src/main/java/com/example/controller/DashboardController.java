package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Optionally, you can pass some data to the dashboard page here
        model.addAttribute("message", "Welcome to the Dashboard!");
        return "dashboard";  // Make sure this matches the actual name of your dashboard HTML page
    }
    @GetMapping("/DocumentService")
    public String uploadDoc(String userId) {
        return "DocumentService"; 
    }
    
    @GetMapping("/PostalService")
    public String postalService(String userId) {
        return "PostalService"; 
    }
}
