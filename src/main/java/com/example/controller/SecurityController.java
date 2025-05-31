package com.example.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.config.QrCodeWebSocketHandler;
import com.example.dto.CustomerDTO;
import com.example.dto.EmailDTO;
import com.example.dto.EmployeeDTO;
import com.example.dto.SecurityDTO;
import com.example.service.ISecrityService;
import com.example.service.EmailService;
import com.example.service.QrCodeService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/Security")
public class SecurityController {

	@Autowired
	private ISecrityService empService;

	@Autowired
	private EmailService otpService;

	@Autowired
	private QrCodeService qrCodeService;

	@Autowired
	private QrCodeWebSocketHandler webSocketHandler;

	@PostMapping(value = "/serch-user", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public com.example.service.ResponseEntity<SecurityDTO> serchUser(@RequestBody SecurityDTO searchRequest,
			@RequestHeader(value = "userId", required = false) String userId) {

		com.example.service.ResponseEntity<SecurityDTO> security = empService.searchUserFromList(searchRequest, userId);

		return security;
	}

	@GetMapping("/user-list")
	public com.example.service.ResponseEntity<List<SecurityDTO>> fetchSecurityRole(
			@RequestParam(value = "id", required = false) String id, @RequestHeader(required = false) String userId) {

		com.example.service.ResponseEntity<List<SecurityDTO>> serviceResponse = new com.example.service.ResponseEntity<>();
		List<SecurityDTO> response = new ArrayList<>();
		try {
			response = empService.fetchListOfUsers(id, userId);
			if (response != null && !response.isEmpty()) {
				serviceResponse.setData(response);
			} else {
				serviceResponse.setErrorMessage("No available users");
			}
		} catch (Exception e) {
			e.getMessage();
		}

		return serviceResponse;
	}

	@PostMapping(value = "/add-user", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public com.example.service.ResponseEntity<SecurityDTO> accessSecurity(@RequestBody SecurityDTO securityDTO,
			@RequestHeader(value = "userId") String userId) {

		com.example.service.ResponseEntity<SecurityDTO> securityResponce = new com.example.service.ResponseEntity<>();
		SecurityDTO security = empService.updateSecurity(securityDTO, userId);

		securityResponce.setData(security);

		return securityResponce;
	}

	@PostMapping(value = "/register-user", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public com.example.service.ResponseEntity<String> registerUser(@RequestBody SecurityDTO securityDTO,
			@RequestHeader(value = "userId") String userId) {

		com.example.service.ResponseEntity<String> data = new com.example.service.ResponseEntity<>();
		String ok = empService.registerUser(securityDTO, userId);
		if (ok.contains("Successfully")) {
			data.setData(ok);
		} else {
			data.setErrorMessage(ok);
		}
		return data;
	}

	@GetMapping("/generateCaptcha")
	public void generateCaptcha(HttpServletResponse response) throws IOException {
		int width = 150, height = 50;

		// Create an image
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = image.createGraphics();

		// Background color
		graphics.setColor(Color.LIGHT_GRAY);
		graphics.fillRect(0, 0, width, height);

		// Generate random text
		String captchaText = generateRandomText(6);

		// Store captchaText in session or cache (not included in this example)
		System.out.println("Generated CAPTCHA: " + captchaText);

		// Set font and draw text
		graphics.setFont(new Font("Arial", Font.BOLD, 26));
		graphics.setColor(Color.BLACK);
		graphics.drawString(captchaText, 20, 35);

		// Add some distortion (lines)
		graphics.setColor(Color.RED);
		graphics.drawLine(0, 25, 150, 25);
		graphics.drawLine(10, 40, 140, 10);

		// Send image as response
		response.setContentType("image/png");
		ImageIO.write(image, "png", response.getOutputStream());
		response.getOutputStream().close();
	}

	// Function to generate random text
	private String generateRandomText(int length) {
		String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			sb.append(chars.charAt(random.nextInt(chars.length())));
		}
		return sb.toString();

	}

	@DeleteMapping("user-list/delete/{id}")
	public org.springframework.http.ResponseEntity<Void> deleteNote(@PathVariable Long id) {
		boolean deleted = empService.deleteNoteById(id);
		return deleted ? org.springframework.http.ResponseEntity.noContent().build()
				: org.springframework.http.ResponseEntity.notFound().build();
	}

	// @Cacheable(value = "otpCache", key = "#email")
	@RequestMapping(value = "/generateOtpService", method = RequestMethod.POST)
	public com.example.service.ResponseEntity<String> sendOtp(
			@RequestParam(value = "Email id", required = true) String email,
			@RequestParam(value = "user id", required = true) String userId) {

		com.example.service.ResponseEntity<String> data = new com.example.service.ResponseEntity<>();

		String ok = otpService.sendOtp(email, userId);
		if (ok.contains("Success")) {
			data.setData(ok);
		} else {
			data.setErrorMessage(ok);
		}
		return data;
	}

	@PostMapping("/verify-otp")
	public com.example.service.ResponseEntity<String> verifyOtp(@RequestParam(value = "Email id") String email,
			@RequestParam(value = "Otp") String otp, @RequestHeader(value = "user-id", required = true) String userId) {
		boolean isValid = otpService.verifyOtp(email, otp, userId);
		com.example.service.ResponseEntity<String> data = new com.example.service.ResponseEntity<>();
		if (isValid) {
			data.setData("Otp Verified Successfully");
		} else {
			data.setErrorMessage("Invalid otp , please enter correct OTP or click on generate otp button");
		}
//			return isValid ? ResponseEntity.ok("OTP verified")
//					: ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid OTP");
		return data;
	}

	@PostMapping(value = "/add-email", consumes = MediaType.APPLICATION_JSON_VALUE)
	public com.example.service.ResponseEntity<String> addEmailService(@RequestBody List<EmailDTO> emailDTO,
			@RequestHeader(value = "userId", required = true) String userId) {

		com.example.service.ResponseEntity<String> emailResp = new com.example.service.ResponseEntity<>();

		String email = otpService.addEmailList(emailDTO, userId);

		if (email != null && !email.isEmpty()) {
			emailResp.setData(email);
		} else {
			emailResp.setErrorMessage("Error while adding email ");
		}
		return emailResp;

	}

	@GetMapping("/fetchEmailids")
	public com.example.service.ResponseEntity<List<EmailDTO>> fetchEmailDetails(
			@RequestParam(value = "Email id", required = false) String id, @RequestHeader String userId) {
		com.example.service.ResponseEntity<List<EmailDTO>> emailResp = new com.example.service.ResponseEntity<>();

		List<EmailDTO> email = otpService.fetchListOfEmailIds(id, userId);

		if (email != null && !email.isEmpty()) {
			emailResp.setData(email);
		} else {
			emailResp.setErrorMessage("Error while fetching email ");
		}
		return emailResp;
	}

	@PostMapping("/send-email")
	public com.example.service.ResponseEntity<String> sendEmail(@RequestParam("to") String to,
			@RequestParam(value = "subject", required = false, defaultValue = "") String subject,
			@RequestParam(value = "body", required = false, defaultValue = "") String body,
			@RequestParam(value = "attachment", required = false) MultipartFile attachment) {
		com.example.service.ResponseEntity<String> response = new com.example.service.ResponseEntity<>();
		try {
			String resp = otpService.sendEmailtoUser(to, subject, body, attachment);
			if (resp.contains("successfully")) {
				response.setData(resp);
			} else {
				response.setData(resp);
			}

		} catch (Exception e) {
			e.getStackTrace();
		}
		return response;
	}

	@PostMapping("/generate")
	public ResponseEntity<?> generateQrToken(@RequestBody Map<String, String> request) {
		String sessionId = request.get("sessionId");
		if (sessionId == null) {
			return ResponseEntity.badRequest().body(Map.of("error", "Session ID required"));
		}
		QrCodeService.QrToken qrToken = qrCodeService.generateToken(sessionId);
		return ResponseEntity.ok(Map.of("token", qrToken.getToken()));
	}

	@PostMapping("/authenticate")
	public ResponseEntity<?> authenticateQrToken(@RequestBody Map<String, String> request) {
		String token = request.get("token");
		String userId = request.get("userId");
		String accessToken = request.get("accessToken"); // From mobile app

		QrCodeService.QrToken qrToken = qrCodeService.validateToken(token);
		if (qrToken == null) {
			return ResponseEntity.badRequest().body(Map.of("error", "Invalid or expired token"));
		}

		// Validate user credentials (e.g., check accessToken against a user database)
		// For demo, assume accessToken is valid if non-empty
		if (userId == null || accessToken == null || accessToken.isEmpty()) {
			return ResponseEntity.badRequest().body(Map.of("error", "Invalid credentials"));
		}

		// Mark token as authenticated
		qrCodeService.authenticateToken(token, userId);

		// Notify browser via WebSocket
		try {
			webSocketHandler.sendAuthStatus(qrToken.getSessionId(), "success", userId);
			qrCodeService.removeToken(token); // Clean up
			return ResponseEntity.ok(Map.of("message", "Authentication successful"));
		} catch (Exception e) {
			return ResponseEntity.status(500).body(Map.of("error", "Failed to notify client"));
		}
	}

	@GetMapping("/customer-details")
	public com.example.service.ResponseEntity<List<CustomerDTO>> getCustomerlDetails(
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "customerNo", required = false) String customerNo, @RequestHeader String userId) {
		com.example.service.ResponseEntity<List<CustomerDTO>> emailResp = new com.example.service.ResponseEntity<>();

		List<CustomerDTO> customerDTO = otpService.getCustomerDetails(email, customerNo, userId);

		if (customerDTO != null && !customerDTO.isEmpty()) {
			emailResp.setData(customerDTO);
		} else {
			emailResp.setErrorMessage("Error while fetching customerDetails ");
		}
		return emailResp;
	}

	@PostMapping("/customer-details/update")
	public com.example.service.ResponseEntity<String> getCustomerlDetails(@RequestBody CustomerDTO customerDTO,
			@RequestHeader String userId) {
		com.example.service.ResponseEntity<String> resp = new com.example.service.ResponseEntity<>();

		String message = otpService.updateCustomerDetails(customerDTO, userId);

		if (message.contains("Success")) {
			resp.setData(message);
		} else {
			resp.setErrorMessage(message);
		}
		return resp;
	}

	@PostMapping("/register-fingerprint")
	public com.example.service.ResponseEntity<String> registerWebAuthn(@RequestBody SecurityDTO request,
			@RequestHeader String userId) {
		com.example.service.ResponseEntity<String> resp = new com.example.service.ResponseEntity<>();
		try {
			String updatedSecurity = empService.registerWebAuthnCredentials(request);
			if (updatedSecurity.contains("Success")) {
				resp.setData(updatedSecurity);
			} else {
				resp.setErrorMessage(updatedSecurity);
			}
		} catch (Exception e) {
			e.getMessage();
		}
		return resp;

	}

	@PostMapping("/login-fingerprint")
	public ResponseEntity<?> loginWithFingerprint(@RequestHeader(value = "userId", required = true) String userId) {
		try {
			boolean login = otpService.fingerprintLogin(userId);
			if (login) {
				return ResponseEntity.ok(new SuccessResponse("Fingerprint login successful"));
			} else {
				return ResponseEntity.badRequest().body(new ErrorResponse("Fingerprint login failed"));
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
		}
	}

	public static class SuccessResponse {
		private String message;

		public SuccessResponse(String message) {
			this.message = message;
		}

		public String getMessage() {
			return message;
		}
	}

	public static class ErrorResponse {
		private String errorMessage;

		public ErrorResponse(String errorMessage) {
			this.errorMessage = errorMessage;
		}

		public String getErrorMessage() {
			return errorMessage;
		}
	}

}
