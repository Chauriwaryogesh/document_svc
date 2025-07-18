package com.SecureAccessPortal.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SecureAccessPortal.Entity.Customer;
import com.SecureAccessPortal.Modal.ChatbotRequest;
import com.SecureAccessPortal.Modal.ChatbotResponse;
import com.SecureAccessPortal.Repo.BankAccountRepo;
import com.SecureAccessPortal.Repo.CustomerRepo;
import com.SecureAccessPortal.Repo.IPolicyRepo;

@Service
public class ChatbotService {

	@Autowired
    private  CustomerRepo customerRepository;
	
	@Autowired
    private  IPolicyRepo policyRepository;
	
	@Autowired
    private  BankAccountRepo bankAccountRepository;

    @Autowired
    public ChatbotService(CustomerRepo customerRepository) {
        this.customerRepository = customerRepository;
    }

    public ChatbotResponse processMessage(ChatbotRequest request) {
        String userId = request.getUserId();
        String message = request.getMessage() != null ? request.getMessage().trim().toUpperCase() : "";
        String location =  "Nagpur";

        // Fetch customer by userId (customerNo)
        Optional<Customer> customerOpt = customerRepository.findByUserCode(userId);
        String customerName = customerOpt.map(Customer::getName).orElse("User");

        // Initialize response
        String greeting;
        List<String> data = new ArrayList<>();

        // Process message
        if ("HI".equals(message)) {
            greeting = "Hi, " + customerName + "!";
        } else if ("POLICY".equals(message)) {
            greeting = "Here are your policy numbers, " + customerName + "!";
            data = policyRepository.findByPoliciesByUserCode(userId);
        } else if ("BANK ACCOUNT".equals(message)) {
            greeting = "Here are your bank account numbers, " + customerName + "!";
            data = bankAccountRepository.findBankAccountByUserCode(userId);
        } else if ("WEATHER".equals(message)) {
            greeting = "Today's weather in " + location + ", " + customerName + "!";
            // Mock weather data for July 13, 2025
            data.add("Date: July 13, 2025");
            data.add("Location: " + location);
            data.add("Temperature: 25°C / 77°F");
            data.add("Condition: Partly cloudy");
            data.add("Humidity: 60%");
        } else {
            greeting = "Hello, " + customerName + "! How can I assist you?";
        }

        // Generate links to application pages
        Map<String, String> links = new HashMap<>();
        links.put("Bank Account", "/bank-account");
        links.put("Workitem", "/workitem");
        links.put("Payments", "/payments");
        links.put("Policy", "/policy");
        links.put("Create Customer", "/create-customer");

        // Build response
        ChatbotResponse response = new ChatbotResponse(greeting, links);
       // response.setData(data); // Add data field to ChatbotResponse
        return response;
    }
}