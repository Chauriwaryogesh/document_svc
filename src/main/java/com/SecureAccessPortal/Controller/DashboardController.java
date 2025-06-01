package com.SecureAccessPortal.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DashboardController {
	
	@GetMapping("/home")
    public String home(String userCode) {
        return "home"; 
    }

	@GetMapping("/forgot-credentials")
    public String forgetCredentials(String userCode) {
        return "forgot-credentials"; 
    }
	@GetMapping("/activity")
    public String activity(String userCode) {
        return "Activity"; 
    }
	@GetMapping("/cloudServices")
    public String cloudServices(String userCode) {
        return "CloudServices"; 
    }
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Optionally, you can pass some data to the dashboard page here
        model.addAttribute("message", "Welcome to the Dashboard!");
        return "dashboard";  // Make sure this matches the actual name of your dashboard HTML page
    }
    @GetMapping("/DocumentService")
    public String uploadDoc(String userCode) {
        return "DocumentService"; 
    }
    
    @GetMapping("/PostalService")
    public String postalService(String userCode) {
        return "PostalService"; 
    }
    @GetMapping("/AdminPanel")
    public String adminService(@RequestParam(value = "id", required = false) String id,
                               @RequestHeader(required = false) String userCode, Model model) {     
        System.out.println("Received ID: " + id);
        System.out.println("Received userCode: " + userCode);              
        if (id != null) {
            model.addAttribute("id", id);        
        }       
        return "AdminPanel"; 
    }


    @GetMapping("/EmployeeService")
    public String employeeService(String userCode) {
        return "EmployeeService"; 
    }
    @GetMapping("/MailServices")
    public String mailService(String userCode) {
        return "MailServices"; 
    }
    
    @GetMapping("/payment")
    public String payment(String userCode) {
        return "payment"; 
    }
}
