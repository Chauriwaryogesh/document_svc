package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

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
    @GetMapping("/AdminPanel")
    public String adminService(@RequestParam(value = "id", required = false) String id,
                               @RequestHeader(required = false) String userId, Model model) {     
        System.out.println("Received ID: " + id);
        System.out.println("Received UserID: " + userId);              
        if (id != null) {
            model.addAttribute("id", id);        
        }       
        return "AdminPanel"; 
    }


    @GetMapping("/EmployeeService")
    public String employeeService(String userId) {
        return "EmployeeService"; 
    }
    @GetMapping("/MailServices")
    public String mailService(String userId) {
        return "MailServices"; 
    }
}
