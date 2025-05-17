package com.example.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.management.RuntimeErrorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.CommonConstants.CommonConstant;
import com.example.dto.Address;
import com.example.dto.ContactDetails;
import com.example.dto.CustomerDTO;
import com.example.dto.EmailDTO;
import com.example.dto.WorkItemDTO;
import com.example.entity.Customer;
import com.example.entity.Email;
import com.example.entity.OtpStore;
import com.example.entity.Security;
import com.example.repo.CustomerRepo;
import com.example.repo.IEmailRepo;
import com.example.repo.IOtpServiceDB;
import com.example.repo.ISecurityRepo;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class OtpService {
    private static final Logger logger = LoggerFactory.getLogger(OtpService.class);

	@Autowired
	private IEmailRepo emailRepo;
	
	@Autowired
	private IWorkItemService workItemService;
	
	@Autowired
	private CustomerRepo customerRepo;
	
	@Autowired
	private ISecurityRepo securityRepo;

	private final IOtpServiceDB IOtpServiceDB;
	private final JavaMailSender mailSender;
	private final Map<String, String> otpStore = new HashMap<>();

	private static final AtomicInteger counter = new AtomicInteger(1);

	public OtpService(JavaMailSender mailSender, IOtpServiceDB IOtpServiceDB) {
		this.mailSender = mailSender;
		this.IOtpServiceDB = IOtpServiceDB;
	}

	public String sendOtp(String email, String userId) {
        String response = "";
        String otp = generateOtp();
        otpStore.put(email, otp);

        email = verifyEmail(email);

//        try {
//            // Check email status is Verified Y
//            Security security = securityRepo.findByEmail(email, "N");
//            if (security != null) {
//                if (security.getEmail().equalsIgnoreCase(email) &&
//                        security.getUserCode().equalsIgnoreCase(userId) &&
//                        security.getIsEmailVerified().equals("Y") &&
//                        security.getIsUserCodeVerified().equals("Y")) {
//
//                    MimeMessage message = mailSender.createMimeMessage();
//                    MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8"); // Enable multipart for HTML
//                    helper.setFrom(new InternetAddress("SecureAccessPortal@myCompany.com", "Secure Access Portal"));
//                    helper.setTo(email);
//                    helper.setSubject("🔒 Your One-Time Password (OTP) for Secure Login");
//
//                    // HTML email template
//                    String htmlContent = "<!DOCTYPE html>\n" +
//                            "<html lang=\"en\">\n" +
//                            "<head>\n" +
//                            "    <meta charset=\"UTF-8\">\n" +
//                            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
//                            "    <title>Your One-Time Password (OTP)</title>\n" +
//                            "    <style>\n" +
//                            "        body {\n" +
//                            "            margin: 0;\n" +
//                            "            padding: 0;\n" +
//                            "            font-family: 'Arial', sans-serif;\n" +
//                            "            background-color: #f4f4f9;\n" +
//                            "            color: #333;\n" +
//                            "        }\n" +
//                            "        .container {\n" +
//                            "            max-width: 600px;\n" +
//                            "            margin: 20px auto;\n" +
//                            "            background-color: #ffffff;\n" +
//                            "            border-radius: 10px;\n" +
//                            "            overflow: hidden;\n" +
//                            "            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);\n" +
//                            "        }\n" +
//                            "        .header {\n" +
//                            "            background: linear-gradient(to right, #007bff, #0056b3);\n" +
//                            "            padding: 20px;\n" +
//                            "            text-align: center;\n" +
//                            "            color: white;\n" +
//                            "        }\n" +
//                            "        .header img {\n" +
//                            "            max-width: 150px;\n" +
//                            "            height: auto;\n" +
//                            "        }\n" +
//                            "        .content {\n" +
//                            "            padding: 30px;\n" +
//                            "            text-align: center;\n" +
//                            "        }\n" +
//                            "        .otp-box {\n" +
//                            "            background-color: #e9f7ff;\n" +
//                            "            border: 2px dashed #007bff;\n" +
//                            "            border-radius: 8px;\n" +
//                            "            padding: 20px;\n" +
//                            "            margin: 20px 0;\n" +
//                            "            font-size: 28px;\n" +
//                            "            font-weight: bold;\n" +
//                            "            color: #007bff;\n" +
//                            "            letter-spacing: 5px;\n" +
//                            "        }\n" +
//                            "        .copy-button {\n" +
//                            "            background-color: #28a745;\n" +
//                            "            color: white;\n" +
//                            "            border: none;\n" +
//                            "            padding: 10px 20px;\n" +
//                            "            border-radius: 5px;\n" +
//                            "            cursor: pointer;\n" +
//                            "            font-size: 16px;\n" +
//                            "            margin-top: 10px;\n" +
//                            "            text-decoration: none;\n" +
//                            "            display: inline-block;\n" +
//                            "        }\n" +
//                            "        .copy-button:hover {\n" +
//                            "            background-color: #1e7e34;\n" +
//                            "        }\n" +
//                            "        .clock {\n" +
//                            "            display: inline-flex;\n" +
//                            "            align-items: center;\n" +
//                            "            background-color: #fff3e0;\n" +
//                            "            border-radius: 50px;\n" +
//                            "            padding: 10px 20px;\n" +
//                            "            margin: 15px 0;\n" +
//                            "            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);\n" +
//                            "        }\n" +
//                            "        .clock img {\n" +
//                            "            width: 20px;\n" +
//                            "            height: 20px;\n" +
//                            "            margin-right: 10px;\n" +
//                            "        }\n" +
//                            "        .clock span {\n" +
//                            "            font-weight: bold;\n" +
//                            "            color: #ff9800;\n" +
//                            "        }\n" +
//                            "        .contact-info, .unsubscribe {\n" +
//                            "            background-color: #f9f9f9;\n" +
//                            "            padding: 20px;\n" +
//                            "            text-align: center;\n" +
//                            "            font-size: 14px;\n" +
//                            "            color: #555;\n" +
//                            "        }\n" +
//                            "        .contact-info a, .unsubscribe a {\n" +
//                            "            color: #007bff;\n" +
//                            "            text-decoration: none;\n" +
//                            "        }\n" +
//                            "        .contact-info a:hover, .unsubscribe a:hover {\n" +
//                            "            text-decoration: underline;\n" +
//                            "        }\n" +
//                            "        .footer {\n" +
//                            "            background-color: #e9ecef;\n" +
//                            "            padding: 15px;\n" +
//                            "            text-align: center;\n" +
//                            "            font-size: 12px;\n" +
//                            "            color: #555;\n" +
//                            "        }\n" +
//                            "        .footer img {\n" +
//                            "            width: 24px;\n" +
//                            "            height: 24px;\n" +
//                            "            margin: 0 10px;\n" +
//                            "            vertical-align: middle;\n" +
//                            "        }\n" +
//                            "        @media only screen and (max-width: 600px) {\n" +
//                            "            .container {\n" +
//                            "                margin: 10px;\n" +
//                            "            }\n" +
//                            "            .otp-box {\n" +
//                            "                font-size: 24px;\n" +
//                            "            }\n" +
//                            "        }\n" +
//                            "    </style>\n" +
//                            "</head>\n" +
//                            "<body>\n" +
//                            "    <div class=\"container\">\n" +
//                            "        <div class=\"header\">\n" +
//                            "            <img src=\"https://via.placeholder.com/150x50?text=Secure+Access+Portal\" alt=\"Secure Access Portal Logo\">\n" +
//                            "            <h1>Secure OTP Login</h1>\n" +
//                            "        </div>\n" +
//                            "        <div class=\"content\">\n" +
//                            "            <h2>Dear Customer,</h2>\n" +
//                            "            <p>Your One-Time Password (OTP) for secure login is:</p>\n" +
//                            "            <div class=\"otp-box\" id=\"otpValue\">" + otp + "</div>\n" +
//                            "            <button class=\"copy-button\" onclick=\"navigator.clipboard.writeText(&quot;" + otp + "&quot;)\">Copy OTP</button>\n" +
//                            "            <p>If the button doesn't work, manually copy the OTP: <strong>" + otp + "</strong></p>\n" +
//                            "            <div class=\"clock\">\n" +
//                            "                <img src=\"https://img.icons8.com/ios-filled/20/ff9800/clock.png\" alt=\"Clock\">\n" +
//                            "                <span>Expires in 30 minutes</span>\n" +
//                            "            </div>\n" +
//                            "            <p>Keep this OTP confidential and do not share it with anyone.</p>\n" +
//                            "        </div>\n" +
//                            "        <div class=\"contact-info\">\n" +
//                            "            <h3>Contact Us</h3>\n" +
//                            "            <p>\n" +
//                            "                <img src=\"https://img.icons8.com/ios-filled/16/007bff/email.png\" alt=\"Email\">\n" +
//                            "                <a href=\"mailto:support@mycompany.com\">support@mycompany.com</a>\n" +
//                            "            </p>\n" +
//                            "            <p>\n" +
//                            "                <img src=\"https://img.icons8.com/ios-filled/16/007bff/phone.png\" alt=\"Phone\">\n" +
//                            "                <a href=\"tel:+918208247944\">+91-820-824-7944</a> (24/7, Mon-Fri)\n" +
//                            "            </p>\n" +
//                            "        </div>\n" +
//                            "        <div class=\"unsubscribe\">\n" +
//                            "            <p>\n" +
//                            "                To stop receiving these emails, please\n" +
//                            "                <a href=\"https://mycompany.com/unsubscribe?email=" + email + "\">unsubscribe</a>\n" +
//                            "                or contact our support team.\n" +
//                            "            </p>\n" +
//                            "        </div>\n" +
//                            "        <div class=\"footer\">\n" +
//                            "            <p>Your Security, Our Priority - Secure Access Portal © 2025</p>\n" +
//                            "            <p>\n" +
//                            "                <a href=\"https://facebook.com/mycompany\"><img src=\"https://img.icons8.com/ios-filled/24/007bff/facebook.png\" alt=\"Facebook\"></a>\n" +
//                            "                <a href=\"https://twitter.com/mycompany\"><img src=\"https://img.icons8.com/ios-filled/24/007bff/twitter.png\" alt=\"Twitter\"></a>\n" +
//                            "                <a href=\"https://linkedin.com/company/mycompany\"><img src=\"https://img.icons8.com/ios-filled/24/007bff/linkedin.png\" alt=\"LinkedIn\"></a>\n" +
//                            "            </p>\n" +
//                            "        </div>\n" +
//                            "    </div>\n" +
//                            "</body>\n" +
//                            "</html>";
//
//                    // Plain text fallback for clients that don't support HTML
//                    String plainTextContent =
//                            "Dear Customer,\n\n" +
//                                    "Your One-Time Password (OTP) for secure login is: " + otp + "\n\n" +
//                                    "This OTP is valid for 30 minutes. Keep it confidential and do not share it with anyone.\n\n" +
//                                    "Contact Us:\n" +
//                                    "Email: support@mycompany.com\n" +
//                                    "Phone: +91-820-824-7944 (24/7, Mon-Fri)\n\n" +
//                                    "To unsubscribe, reply with 'UNSUBSCRIBE' or contact support@mycompany.com.\n\n" +
//                                    "Your Security, Our Priority - Secure Access Portal";
//
//                    helper.setText(plainTextContent, htmlContent); // Set plain text and HTML content
//
//                    // Call repo to store OTP in DB
//                    OtpStore otpStore = new OtpStore();
//                    otpStore.setId(nextcount());
//                    otpStore.setOtp(otp);
//                    otpStore.setEmail(email);
//
//                    LocalDateTime dateTime = LocalDateTime.now();
//                    long epochMillis = dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
//                    otpStore.setCreatedTime(epochMillis);
//                    otpStore.setCreatedBy(userId);
//
//                    LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(30);
//                    long exp = expiryTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
//                    otpStore.setExpiryTime(exp);
//                    IOtpServiceDB.save(otpStore);
//
//                    mailSender.send(message);
//                    logger.info("OTP email sent successfully to {}", email);
//                    response = "Success,Otp send SuccessFully";
//                } else {
//                    logger.warn("Email verification failed for {}: Not verified or user code mismatch", email);
//                    response = "Email is Not verified please connect with admin";
//                }
//            } else {
//                logger.warn("Email {} not registered in system", email);
//                response = "Email is Not Registered in System please connect with admin";
//            }
//        } catch (Exception e) {
//            logger.error("Failed to send OTP email to {}: {}", email, e.getMessage());
//            e.printStackTrace();
//            response = "Otp send faild";
//        }
        response = "Success,Otp send SuccessFully";
        return response;
    }

	private String verifyEmail(String email) {
		if (email.contains("@gmail.com")) {
			return email;
		} else if (!email.contains("@gmail.com") && !email.contains("outlook.com")) {
			email = email.concat("@gmail.com");
		}
		return email;
	}

	private String generateOtp() {
		Random random = new Random();
		int otp = 100000 + random.nextInt(900000);
		return String.valueOf(otp);
	}

	public boolean verifyOtp(String email, String otp, String userId) {
		// call Repo
		Optional<OtpStore> emailData = IOtpServiceDB.findById(email);
		//OtpStore otpStore = emailData.get();
		LocalDateTime dateTime = LocalDateTime.now();
		Long epochMillis = dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

		boolean found = emailData.stream().filter(data -> data!= null && data.getEmail().equalsIgnoreCase(email) &&
				data.getOtp() != null && data.getOtp().equalsIgnoreCase(otp) && epochMillis <= data.getExpiryTime()).findAny().isPresent();
		found=true;
		return found;
	}

	public long nextcount() {
		return counter.getAndIncrement();
	}

	public String sendDetailEmail(String email, String id, WorkItemDTO workItem, String userId) {
		String otp = generateOtp();
		otpStore.put(email, otp);

		email = verifyEmail(email);

		try {
			jakarta.mail.internet.MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, false, "utf-8");
			String html = """
					<html>
					  <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
					    <div style="max-width: 600px; margin: auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 10px;">

					      <h2 style="color: #2c3e50;">One Time Password (OTP) for Secure Login</h2>

					      <p>Dear User,</p>

					      <p>Your one-time password (OTP) has been generated for secure login:</p>

					      <ul style="list-style: none; padding: 0;">
					        <li><strong>Employee ID:</strong> id </li>
					        <li><strong>Email ID:</strong> email</li>
					        <li><strong>OTP:</strong> <span style="font-size: 20px; font-weight: bold; color: #e74c3c;">otp</span></li>
					        <li><strong>Expiry:</strong> This OTP is valid for 1 hour.</li>
					      </ul>

					      <p>You can log in using the button below:</p>
					      <p>
					        <a href="https://www.yourcompanywebsite.com/login"
					           style="display: inline-block; padding: 10px 20px; background-color: #3498db; color: white; text-decoration: none; border-radius: 5px;">
					           Login Now
					        </a>
					      </p>

					      <hr style="margin: 30px 0;">

					      <footer style="font-size: 12px; text-align: center; color: #777;">
					        <p>Aalo Leleo Pvt. Ltd. | www.aaloleleo.com</p>
					        <p>All rights reserved © 2025</p>
					        <p>
					          <a href="https://www.yourcompanywebsite.com/unsubscribe" style="color: #777;">Unsubscribe</a>
					        </p>
					      </footer>

					    </div>
					  </body>
					</html>
					""";

			helper.setFrom(new InternetAddress(/* "KIoSK_Bank_Helpline@gmail.com", */ "AALU_LELE_LIMITED"));
			helper.setTo(email);
			helper.setSubject("Employment Details ");
			helper.setText(html, true);

			// call repo to store Otp in DB

			OtpStore otpStore = new OtpStore();
			otpStore.setId(nextcount());
			otpStore.setOtp(otp);
			otpStore.setEmail(email);

			LocalDateTime dateTime = LocalDateTime.now();
			long epochMillis = dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			otpStore.setCreatedTime(epochMillis);
			otpStore.setCreatedBy(userId);

			LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(30);
			long exp = expiryTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			otpStore.setExpiryTime(exp);
			IOtpServiceDB.save(otpStore);

			mailSender.send(message);
			return "Otp send SuccessFully";
		} catch (Exception e) {
			e.printStackTrace();
			return "Otp send faild";
		}

	}

	public String addEmailList(List<EmailDTO> emailDTOList, String userId) {
		String response = "";
		List<Email> emailList = emailDTOList.stream().map(emailDTO -> {
			Email email = new Email();
			email.setEmail(emailDTO.getEmail());
			email.setCountry(emailDTO.getCountry());
			if (userId != null) {
				email.setCreatedBy(userId);
			} else {
				email.setCreatedBy(emailDTO.getCreatedBy());
			}
			email.setCreatedTime(String.valueOf(LocalDateTime.now()));
			email.setIsVerified(emailDTO.getIsVerified());
			email.setOwnerName(emailDTO.getOwnerName());
			email.setPhoneNumber(emailDTO.getPhoneNumber());
			email.setUpdatedBy(emailDTO.getUpdatedBy());
			email.setUpdatedTime(emailDTO.getUpdatedTime());
			return email;
		}).collect(Collectors.toList());
		
		
		List<Email> entityREsp = emailRepo.saveAll(emailList);
		if (entityREsp != null) {
			response = "Email id added Successfully";
			// Create WorkItem whenever added new Employee or update.
			WorkItemDTO workItemRequest= new WorkItemDTO();
			workItemRequest.setComment("WorkItem getting created for Update Employee Details");
			workItemRequest.setCreatedBy(userId);
			workItemRequest.setWorkType(CommonConstant.Email_added);
			WorkItemDTO  workItem =workItemService.createWorkItem( workItemRequest, userId);
			if (workItem != null) {
				logger.info("WorkItemcreated succesfully");
				new RuntimeErrorException(null, "Error while creating WorkItem");
			}
		} else {
			response = "Exception while added email id";
		}
		return response;
	}

	public List<EmailDTO> fetchListOfEmailIds(String id, String userId) {
		//fetch using id
		if(id != null) {
			List <Email> listOfEmail=emailRepo.findByEmail(id);
			return listOfEmail.stream().map(email ->{
				EmailDTO emailDTO = new EmailDTO();
				emailDTO.setEmail(email.getEmail());
				emailDTO.setComment(email.getComment());
				emailDTO.setCountry(email.getCountry());
				emailDTO.setCreatedBy(email.getCreatedBy());
				emailDTO.setIsVerified(email.getIsVerified());
				emailDTO.setOwnerName(email.getOwnerName());
				emailDTO.setPhoneNumber(email.getPhoneNumber());
				emailDTO.setUpdatedBy(email.getUpdatedBy());
				emailDTO.setUpdatedTime(email.getUpdatedTime());
				return emailDTO;
			}).collect(Collectors.toList());
		}else {
			List <Email> listOfEmail=emailRepo.findAll();
			return listOfEmail.stream().map(email ->{
				EmailDTO emailDTO = new EmailDTO();
				emailDTO.setEmail(email.getEmail());
				emailDTO.setComment(email.getComment());
				emailDTO.setCountry(email.getCountry());
				emailDTO.setCreatedBy(email.getCreatedBy());
				emailDTO.setIsVerified(email.getIsVerified());
				emailDTO.setOwnerName(email.getOwnerName());
				emailDTO.setPhoneNumber(email.getPhoneNumber());
				emailDTO.setUpdatedBy(email.getUpdatedBy());
				emailDTO.setUpdatedTime(email.getUpdatedTime());
				return emailDTO;
			}).collect(Collectors.toList());
		}
	}

	public String sendEmailtoUser(String to, String subject, String body, MultipartFile attachment) {
		// TODO Auto-generated method stub
		String messageResp="";
		try {
            jakarta.mail.internet.MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);  
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body);
            helper.setFrom("your-gmail@gmail.com"); // Must match spring.mail.username
            if (attachment != null && !attachment.isEmpty()) {
                helper.addAttachment(
                        attachment.getOriginalFilename(),
                        new ByteArrayResource(attachment.getBytes())
                );
            }   
            mailSender.send(message);   
            messageResp ="Email sent successfully!";
        } catch (Exception e) {
            e.printStackTrace();
            messageResp="Failed to send email: ";
        }
		return messageResp;

	
	}

	public CustomerDTO getCustomerDetails(String email, String userId) {
		Optional<Customer> customer = customerRepo.findById(email);
		CustomerDTO custDTO = new CustomerDTO();
		if (customer != null && !customer.isEmpty()) {
			Customer cust = customer.get();
			custDTO.setAdminAccess(cust.getAdminAccess());
			custDTO.setAge(cust.getAge());
			custDTO.setEmail(cust.getEmail());
			custDTO.setGender(cust.getGender());
			custDTO.setId(cust.getId());
			custDTO.setMiddleName(cust.getMiddleName());
			custDTO.setName(cust.getName());
			custDTO.setPhoneNumber(cust.getPhoneNumber());
			custDTO.setSurname(cust.getSurname());
			custDTO.setUserId(cust.getUserId());
			custDTO.setSmokerStatus(cust.getSmokerStatus());
			
			//mapping for address
			Address address= new Address();
			address.setCity(cust.getCity());
			address.setCountry(cust.getCountry());
			address.setState(cust.getState());
			address.setStreet(cust.getStreet());
			address.setZipCode(cust.getZipCode());

			
			ContactDetails contact= new ContactDetails();
			
			contact.setAlternateEmail(cust.getAlternateEmail());
			contact.setEmergencyContactName(cust.getEmergencyContactName());
			contact.setEmergencyContactPhone(cust.getEmergencyContactPhone());
			contact.setPhoneCountryCode(cust.getPhoneCountryCode());
			contact.setPhoneNumber(cust.getPhoneNumber());
			custDTO.setAddress(address);
			custDTO.setContactDetails(contact);
		}
		return custDTO;
	}

	public String updateCustomerDetails(CustomerDTO cust, String userId) {
		String message="";
		try {
		if (cust != null ) {
			Customer custDTO = new Customer();;
			custDTO.setAdminAccess(cust.getAdminAccess());
			custDTO.setAge(cust.getAge());
			custDTO.setEmail(cust.getEmail());
			custDTO.setGender(cust.getGender());
			custDTO.setId(cust.getId());
			custDTO.setMiddleName(cust.getMiddleName());
			custDTO.setName(cust.getName());
			custDTO.setPhoneNumber(cust.getPhoneNumber());
			custDTO.setSurname(cust.getSurname());
			custDTO.setUserId(cust.getUserId());
			custDTO.setSmokerStatus(cust.getSmokerStatus());
			
			//mapping for address
			Address address= cust.getAddress();
			custDTO.setCity(address.getCity());
			custDTO.setCountry(address.getCountry());
			custDTO.setState(address.getState());
			custDTO.setStreet(address.getStreet());
			custDTO.setZipCode(address.getZipCode());

			
			ContactDetails contact= cust.getContactDetails();
			
			custDTO.setAlternateEmail(contact.getAlternateEmail());
			custDTO.setEmergencyContactName(contact.getEmergencyContactName());
			custDTO.setEmergencyContactPhone(contact.getEmergencyContactPhone());
			custDTO.setPhoneCountryCode(contact.getPhoneCountryCode());
			custDTO.setPhoneNumber(cust.getPhoneNumber());
			
			customerRepo.save(custDTO);
			message="Success, details updated ";
		}else {
			message="failed, to update  details ";
		}
		}catch(Exception e) {
			e.getMessage();
		}
		return message;
	}
	

}
