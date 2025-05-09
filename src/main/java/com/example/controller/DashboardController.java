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
                               @RequestHeader(required = false) String userId) {
        // Use the id to retrieve data or pass it to the model
        System.out.println("Received ID: " + id);
        
        // Here you can pass the data to the view using a model
        // If you need to return data to the frontend, you can add the data to the model:
        return "AdminPanel"; // You can use the 'id' to filter data for the page
    }

    @GetMapping("/EmployeeService")
    public String employeeService(String userId) {
        return "EmployeeService"; 
    }
}
