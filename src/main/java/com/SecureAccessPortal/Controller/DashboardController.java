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
		return "dashboard"; // Make sure this matches the actual name of your dashboard HTML page
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

	@GetMapping("/verification")
	public String verification(String userCode) {
		return "verification";
	}

	@GetMapping("/bankAccount")
	public String bakAccount(String userCode) {
		return "bankAccount";
	}

	@GetMapping("/workItemServices")
	public String workItemServices(String email, String userCode) {
		return "workItemServices";
	}

	@GetMapping("/workitem-details")
	public String workItemDetails(@RequestParam(value = "ref", required = true) String ref, String userCode) {
		return "workitem-details";
	}

	@GetMapping("/policy")
	public String policy(String userCode) {
		return "policy";
	}

	@GetMapping("/PolicyAdmin")
	public String PolicyAdmin(String userCode) {
		return "PolicyAdmin";
	}
	
	@GetMapping("/PolicySurrenderAdmin")
	public String SurrenderPolicy(@RequestParam(required = false) String customerNo,
			@RequestParam(required = false) String policyNo,String userCode) {
		return "PolicySurrenderAdmin";
	}
	
	@GetMapping("/PolicyClaimAdmin")
	public String policyClaimAdmin(@RequestParam(required = false) String customerNo,
			@RequestParam(required = false) String policyNo,String userCode) {
		return "PolicyClaimAdmin";
	}
	@GetMapping("/AddNotes")
	public String AddNotes(@RequestParam(required = false) String customerNo,
			@RequestParam(required = false) String policyNo,String userCode) {
		return "AddNotes";
	}

	@GetMapping("/Complaints")
	public String Complaints(String userCode) {
		return "Complaints";
	}

	@GetMapping("/Feedback")
	public String Feedback(String userCode) {
		return "Feedback";
	}

	@GetMapping("/CustomerService")
	public String CustomerService(String userCode) {
		return "CustomerService";
	}

	@GetMapping("/ComplaintsForCustomer")
	public String ComplaintsForCustomer(@RequestParam(required = false) String customerNo,
			@RequestParam(required = false) String message, String userCode) {
		return "ComplaintsForCustomer";
	}

	@GetMapping("/PaymentForCustomer")
	public String PaymentForCustomer(@RequestParam(required = false) String customerNo,
			@RequestParam(required = false) String policyNo, @RequestParam(required = false) String paymentId,
			String userCode) {
		return "PaymentForCustomer";
	}
	@GetMapping("/SurrenderPolicyForCustomer")
	public String SurrenderPolicyForCustomer(@RequestParam(required = false) String customerNo,
			@RequestParam(required = false) String policyNo,String userCode) {
		return "SurrenderPolicyForCustomer";
	}
}
