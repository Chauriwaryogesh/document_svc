package com.example.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.SecurityDTO;
import com.example.service.IEmployeeService;
import com.example.service.ResponseEntity;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/Security")
public class SecurityController {
	
	@Autowired
	private IEmployeeService empService;
	
	@GetMapping("/user-list")
	public ResponseEntity<List<SecurityDTO>> fetchSecurityRole(
			@RequestParam(value ="id",required =false) String id,
			@RequestHeader(required =false) String userId) {

		ResponseEntity<List<SecurityDTO>> serviceResponse = new ResponseEntity<>();
		List<SecurityDTO> response = new ArrayList<>();
		try {
			response = empService.fetchListOfUsers(id,userId);

			serviceResponse.setData(response);
		} catch (Exception e) {
			e.getMessage();
		}

		return serviceResponse;
	}
	
	@PostMapping(value ="/add-user" , consumes=MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SecurityDTO> accessSecurity(@RequestBody SecurityDTO securityDTO,
    		@RequestHeader(value="userId") String userId){
		
		ResponseEntity<SecurityDTO> securityResponce= new ResponseEntity<>();
		SecurityDTO security= empService.updateSecurity(securityDTO, userId);
		
		securityResponce.setData(security);
		
		return securityResponce;
	}
	
	
	@PostMapping(value ="/register-user" , consumes=MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> registerUser(@RequestBody SecurityDTO securityDTO,
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

}
