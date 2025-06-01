package com.SecureAccessPortal.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String servicePage(String email,String userCode) {
        return "ServicePage";
    }
    
    @GetMapping("/workItemServices")
    public String workItemServices(String email,String userCode) {
        return "workItemServices";
    }
    
    @GetMapping("/workitem-details")
    public String workItemDetails( @RequestParam(value="ref",required=true) String ref,String userCode) {
        return "workitem-details";
    }
    
    @GetMapping("/policy")
    public String policy(String userCode) {
        return "policy";
    }
    
}

