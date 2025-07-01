package com.SecureAccessPortal.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.management.RuntimeErrorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.SecureAccessPortal.CommonConstants.CommonConstant;
import com.SecureAccessPortal.Entity.Customer;
import com.SecureAccessPortal.Entity.Email;
import com.SecureAccessPortal.Entity.OtpStore;
import com.SecureAccessPortal.Entity.Security;
import com.SecureAccessPortal.Exception.DuplicateEntryException;
import com.SecureAccessPortal.Modal.Address;
import com.SecureAccessPortal.Modal.ContactDetails;
import com.SecureAccessPortal.Modal.CustomerDTO;
import com.SecureAccessPortal.Modal.EmailDTO;
import com.SecureAccessPortal.Modal.WorkItemDTO;
import com.SecureAccessPortal.Repo.CustomerRepo;
import com.SecureAccessPortal.Repo.IEmailRepo;
import com.SecureAccessPortal.Repo.ISecurityRepo;
import com.SecureAccessPortal.Repo.OtpStoreRepo;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
	private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

	@Autowired
	private IEmailRepo emailRepo;

	@Autowired
	private IWorkItemService workItemService;

	@Autowired
	private CustomerRepo customerRepo;

	@Autowired
	private ISecurityRepo securityRepo;

	@Autowired
	private OtpStoreRepo otpStoreRepository;
	
	@Autowired
	private ISecrityService securityService;

	private final JavaMailSender mailSender;
	private final Map<String, String> otpStore = new HashMap<>();

	private static final AtomicInteger counter = new AtomicInteger(1);

	public EmailService(JavaMailSender mailSender, OtpStoreRepo otpStoreRepository) {
		this.mailSender = mailSender;
		this.otpStoreRepository = otpStoreRepository;
	}

	@Transactional
	public String sendOtp(String email, String userCode) {
		String response = "";
		String otp = generateOtp();
		Security security = null;

		try {
			if (email != null && email.contains("@")) {
				security = securityRepo.findByEmail(email, "N");
			} else if (userCode != null) {
				security = securityRepo.findByUserCodeDeletedN(userCode, "N");
			}else {
				logger.error("Invalid email/userCode: {}");
				response = "Invalid email/userCode";
				return response;
			}
			if (security != null) {
				if (security.getEmail().equalsIgnoreCase(email) && "Y".equals(security.getIsEmailVerified())
						&& "Y".equals(security.getIsUserCodeVerified())) {

					try {
						otpStoreRepository.markAsDeletedByEmail(email, userCode, LocalDateTime.now());
					} catch (Exception e) {
						logger.warn("No OTPs found to mark as deleted for email: {}, userCode: {}", email, userCode);
					}
					// Create email content
                    MimeMessage message = mailSender.createMimeMessage();
                    MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
                    helper.setFrom(new InternetAddress("SecureAccessPortal@myCompany.com", "Secure Access Portal"));
                    helper.setTo(email);
                    helper.setSubject("🔒 Your One-Time Password (OTP) for Secure Login");

                    String htmlContent = "<!DOCTYPE html>\n" +
                        "<html lang=\"en\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                        "    <title>Your One-Time Password (OTP)</title>\n" +
                        "    <style>\n" +
                        "        body { margin: 0; padding: 0; font-family: 'Arial', sans-serif; background-color: #f4f4f9; color: #333; }\n" +
                        "        .container { max-width: 600px; margin: 20px auto; background-color: #ffffff; border-radius: 10px; overflow: hidden; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); }\n" +
                        "        .header { background: linear-gradient(to right, #007bff, #0056b3); padding: 20px; text-align: center; color: white; }\n" +
                        "        .header img { max-width: 150px; height: auto; }\n" +
                        "        .content { padding: 30px; text-align: center; }\n" +
                        "        .otp-box { background-color: #e9f7ff; border: 2px dashed #007bff; border-radius: 8px; padding: 20px; margin: 20px 0; font-size: 28px; font-weight: bold; color: #007bff; letter-spacing: 5px; }\n" +
                        "        .copy-button { background-color: #28a745; color: white; border: none; padding: 10px 20px; border-radius: 5px; cursor: pointer; font-size: 16px; margin-top: 10px; text-decoration: none; display: inline-block; }\n" +
                        "        .copy-button:hover { background-color: #1e7e34; }\n" +
                        "        .clock { display: inline-flex; align-items: center; background-color: #fff3e0; border-radius: 50px; padding: 10px 20px; margin: 15px 0; box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); }\n" +
                        "        .clock img { width: 20px; height: 20px; margin-right: 10px; }\n" +
                        "        .clock span { font-weight: bold; color: #ff9800; }\n" +
                        "        .contact-info, .unsubscribe { background-color: #f9f9f9; padding: 20px; text-align: center; font-size: 14px; color: #555; }\n" +
                        "        .contact-info a, .unsubscribe a { color: #007bff; text-decoration: none; }\n" +
                        "        .contact-info a:hover, .unsubscribe a:hover { text-decoration: underline; }\n" +
                        "        .footer { background-color: #e9ecef; padding: 15px; text-align: center; font-size: 12px; color: #555; }\n" +
                        "        .footer img { width: 24px; height: 24px; margin: 0 10px; vertical-align: middle; }\n" +
                        "        @media only screen and (max-width: 600px) { .container { margin: 10px; } .otp-box { font-size: 24px; } }\n" +
                        "    </style>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "    <div class=\"container\">\n" +
                        "        <div class=\"header\">\n" +
                        "            <img src=\"https://via.placeholder.com/150x50?text=Secure+Access+Portal\" alt=\"Secure Access Portal Logo\">\n" +
                        "            <h1>Secure OTP Login</h1>\n" +
                        "        </div>\n" +
                        "        <div class=\"content\">\n" +
                        "            <h2>Dear Customer,</h2>\n" +
                        "            <p>Your One-Time Password (OTP) for secure login is:</p>\n" +
                        "            <div class=\"otp-box\" id=\"otpValue\">" + otp + "</div>\n" +
                        "            <button class=\"copy-button\" onclick=\"navigator.clipboard.writeText('" + otp + "')\">Copy OTP</button>\n" +
                        "            <p>If the button doesn't work, manually copy the OTP: <strong>" + otp + "</strong></p>\n" +
                        "            <div class=\"clock\">\n" +
                        "                <img src=\"https://img.icons8.com/ios-filled/20/ff9800/clock.png\" alt=\"Clock\">\n" +
                        "                <span>Expires in 30 minutes</span>\n" +
                        "            </div>\n" +
                        "            <p>Keep this OTP confidential and do not share it with anyone.</p>\n" +
                        "        </div>\n" +
                        "        <div class=\"contact-info\">\n" +
                        "            <h3>Contact Us</h3>\n" +
                        "            <p>\n" +
                        "                <img src=\"https://img.icons8.com/ios-filled/16/007bff/email.png\" alt=\"Email\">\n" +
                        "                <a href=\"mailto:support@mycompany.com\">support@mycompany.com</a>\n" +
                        "            </p>\n" +
                        "            <p>\n" +
                        "                <img src=\"https://img.icons8.com/ios-filled/16/007bff/phone.png\" alt=\"Phone\">\n" +
                        "                <a href=\"tel:+918208247944\">+91-820-824-7944</a> (24/7, Mon-Fri)\n" +
                        "            </p>\n" +
                        "        </div>\n" +
                        "        <div class=\"unsubscribe\">\n" +
                        "            <p>\n" +
                        "                To stop receiving these emails, please\n" +
                        "                <a href=\"https://mycompany.com/unsubscribe?email=" + email + "\">unsubscribe</a>\n" +
                        "                or contact our support team.\n" +
                        "            </p>\n" +
                        "        </div>\n" +
                        "        <div class=\"footer\">\n" +
                        "            <p>Your Security, Our Priority - Secure Access Portal © 2025</p>\n" +
                        "            <p>\n" +
                        "                <a href=\"https://facebook.com/mycompany\"><img src=\"https://img.icons8.com/ios-filled/24/007bff/facebook.png\" alt=\"Facebook\"></a>\n" +
                        "                <a href=\"https://twitter.com/mycompany\"><img src=\"https://img.icons8.com/ios-filled/24/007bff/twitter.png\" alt=\"Twitter\"></a>\n" +
                        "                <a href=\"https://linkedin.com/company/mycompany\"><img src=\"https://img.icons8.com/ios-filled/24/007bff/linkedin.png\" alt=\"LinkedIn\"></a>\n" +
                        "            </p>\n" +
                        "        </div>\n" +
                        "    </div>\n" +
                        "</body>\n" +
                        "</html>";

                    String plainTextContent = "Dear Customer,\n\n" +
                        "Your One-Time Password (OTP) for secure login is: " + otp + "\n\n" +
                        "This OTP is valid for 30 minutes. Keep it confidential and do not share it with anyone.\n\n" +
                        "Contact Us:\n" +
                        "Email: support@mycompany.com\n" +
                        "Phone: +91-820-824-7944 (24/7, Mon-Fri)\n\n" +
                        "To unsubscribe, reply with 'UNSUBSCRIBE' or contact support@mycompany.com.\n\n" +
                        "Your Security, Our Priority - Secure Access Portal";

                    helper.setText(plainTextContent, htmlContent);

                    // Store OTP in DB
                    OtpStore otpStore = new OtpStore();
                    otpStore.setId(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE); // Generate unique ID
                    otpStore.setOtp(otp);
                    otpStore.setEmail(email);
                    otpStore.setUserCode(userCode);
                    otpStore.setCreatedTime(LocalDateTime.now());
                    otpStore.setCreatedBy(userCode);
                    otpStore.setExpiryTime(LocalDateTime.now().plusMinutes(30));
                    otpStore.setDeletedFlag("N");
                    OtpStore otpStoreDb = otpStoreRepository.save(otpStore);

                    mailSender.send(message);

                    // Customer and workitem logic
                    Optional<Customer> byEmail = customerRepo.findByEmail(email,"N");
                    if (byEmail.isPresent()) {
                        Customer customer = byEmail.get();
                        String workType = CommonConstant.OTP_CREATED;
                        String workItemName = CommonConstant.OTP_SEND_WORKITEM;
                        String comment = "OTP has been sent to customer " + customer.getCustomerNo() + 
                            " for Email " + otpStoreDb.getEmail() + " and userCode is " + otpStoreDb.getUserCode();
                        workItemService.mapRequetforWorkItemOtpService(userCode, customer, workType, workItemName, comment, otpStoreDb);
                    }

					logger.info("OTP email sent successfully to {}", email);
					response = "Success, OTP sent successfully";
				} else {
					logger.warn("Email verification failed for {}: Not verified or user code mismatch", email);
					response = "Email is not verified, please contact admin";
				}
			} else {
				logger.warn("Email {} not registered in system", email);
				response = "Email is not registered in system, please contact admin";
			}
		} catch (Exception e) {
			logger.error("Failed to send OTP email to {}: {}", email, e.getMessage(), e);
			response = "OTP send failed";
		}
		return response;
	}

	

	private String generateOtp() {
		Random random = new Random();
		int otp = 100000 + random.nextInt(900000);
		return String.valueOf(otp);
	}

	public boolean verifyOtp(String email, String otp, String userCode) {
		// Validate OTP with deletedFlag = 'N'
		Optional<OtpStore> emailData = otpStoreRepository.findByEmailAndOtpAndDeletedFlag(email, otp, "N");
		if (!emailData.isPresent()) {
			logger.warn("No valid OTP found for email: {}", email);
			return false;
		}
		OtpStore otpStore = emailData.get();
		LocalDateTime now = LocalDateTime.now();

		// Check OTP validity and expiry
		if (!otpStore.getEmail().equalsIgnoreCase(email) || !otpStore.getOtp().equalsIgnoreCase(otp)
				|| !otpStore.getUserCode().equalsIgnoreCase(userCode) || now.isAfter(otpStore.getExpiryTime())) {
			logger.warn("OTP verification failed for email: {}. Invalid OTP, userCode, or expired.", email);
			return false;
		}

		// Mark OTP as used
		otpStore.setDeletedFlag("Y");
		otpStore.setUpdatedBy(userCode);
		otpStore.setUpdatedTime(LocalDateTime.now());
		otpStoreRepository.save(otpStore);

		// Customer and workitem logic
		Optional<Customer> byEmail = customerRepo.findByEmail(email,"N");
		if (byEmail.isPresent()) {
			Customer customer = byEmail.get();
			String workType = CommonConstant.OTP_VERIFIED;
			String workItemName = CommonConstant.OTP_VERIFIED_WORKITEM;
			String comment = "OTP has been verified successfully for customer " + customer.getCustomerNo()
					+ " for email " + otpStore.getEmail() + " and userCode " + otpStore.getUserCode();
			// TODO: Add bank account policy check
			workItemService.mapRequetforWorkItemOtpService(userCode, customer, workType, workItemName, comment,
					otpStore);
			securityService.logLoginAttempt(email, userCode, true, "Otp Matches","otp");
		} else {
			logger.warn("Customer not found for email: {}", email);
		}

		logger.info("OTP verified successfully for email: {}", email);
		return true;
	}

	public long nextcount() {
		return counter.getAndIncrement();
	}

	public String sendDetailEmail(String email, String id, WorkItemDTO workItem, String userCode) {
		String otp = generateOtp();
		otpStore.put(email, otp);
		String messgae="";
		if (!email.contains("@")) {
			logger.warn("Invalid email address", email);
			messgae = "Invalid email address, please enter correct email address";
			return messgae;
		} 
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
			otpStore.setCreatedTime(LocalDateTime.now());
			otpStore.setCreatedBy(userCode);
			otpStore.setExpiryTime(LocalDateTime.now().plusMinutes(30));
			otpStoreRepository.save(otpStore);

			mailSender.send(message);
			return "Otp send SuccessFully";
		} catch (Exception e) {
			e.printStackTrace();
			return "Otp send faild";
		}

	}

	public String addEmailList(List<EmailDTO> emailDTOList, String userCode) {
		String response = "";
		List<Email> emailList = emailDTOList.stream().map(emailDTO -> {
			Email email = new Email();
			email.setEmail(emailDTO.getEmail());
			email.setCountry(emailDTO.getCountry());
			if (userCode != null) {
				email.setCreatedBy(userCode);
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
			WorkItemDTO workItemRequest = new WorkItemDTO();
			workItemRequest.setComment("WorkItem getting created for Update Employee Details");
			workItemRequest.setCreatedBy(userCode);
			workItemRequest.setWorkType(CommonConstant.Email_added);
			WorkItemDTO workItem = workItemService.createWorkItem(workItemRequest, userCode);
			if (workItem != null) {
				logger.info("WorkItemcreated succesfully");
				new RuntimeErrorException(null, "Error while creating WorkItem");
			}
		} else {
			response = "Exception while added email id";
		}
		return response;
	}

	public List<EmailDTO> fetchListOfEmailIds(String id, String allEmails, String emailVerified,
			String userCodeVerified, String adminAccess, String inActive, String userCode) {
		// fetch using id
		List<Security> securityList = securityRepo.findAll();
		List<EmailDTO> emailInfo = new ArrayList<EmailDTO>();
		if (id != null) {
			List<Email> listOfEmail = emailRepo.findByEmail(id);
			return listOfEmail.stream().map(email -> {
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
		} else if (allEmails != null) {
			emailInfo = securityList.stream().map(sec -> {
				EmailDTO emailDTO = new EmailDTO();
				emailDTO.setEmail(sec.getEmail());
				emailDTO.setUserCode(sec.getUserCode());
				long count = securityList.stream().count();
				emailDTO.setCount(count);
				return emailDTO;
			}).collect(Collectors.toList());
		} else if (emailVerified != null) {
			emailInfo = securityList.stream()
					.filter(sec -> sec.getIsEmailVerified() != null && sec.getIsEmailVerified().equalsIgnoreCase("Y"))
					.map(sec -> {
						EmailDTO emailDTO = new EmailDTO();
						emailDTO.setEmail(sec.getEmail());
						emailDTO.setUserCode(sec.getUserCode());
						long count = securityList.stream().filter(secr -> sec.getIsEmailVerified() != null
								&& secr.getIsEmailVerified().equalsIgnoreCase("Y")).count();
						emailDTO.setCount(count);
						return emailDTO;
					}).collect(Collectors.toList());
		} else if (userCodeVerified != null) {
			emailInfo = securityList.stream().filter(
					sec -> sec.getIsUserCodeVerified() != null && sec.getIsUserCodeVerified().equalsIgnoreCase("Y"))
					.map(sec -> {
						EmailDTO emailDTO = new EmailDTO();
						emailDTO.setEmail(sec.getEmail());
						emailDTO.setUserCode(sec.getUserCode());
						long count = securityList.stream().filter(secr -> sec.getIsUserCodeVerified() != null
								&& secr.getIsUserCodeVerified().equalsIgnoreCase("Y")).count();
						emailDTO.setCount(count);
						return emailDTO;
					}).collect(Collectors.toList());
		} else if (adminAccess != null) {
			List<Customer> all = customerRepo.findAll();
			emailInfo = all.stream()
					.filter(sec -> sec.getAdminAccess() != null && sec.getAdminAccess().equalsIgnoreCase("Y"))
					.map(sec -> {
						EmailDTO emailDTO = new EmailDTO();
						emailDTO.setEmail(sec.getEmail());
						emailDTO.setUserCode(sec.getUserCode());
						long count = all.stream().filter(
								secr -> secr.getAdminAccess() != null && secr.getAdminAccess().equalsIgnoreCase("Y"))
								.count();
						emailDTO.setCount(count);
						return emailDTO;
					}).collect(Collectors.toList());
		} else if (inActive != null) {
			emailInfo = securityList.stream()
					.filter(sec -> sec.getIsEmailVerified() != null && sec.getIsEmailVerified().equalsIgnoreCase("N")
							&& sec.getIsUserCodeVerified() != null && sec.getIsUserCodeVerified().equalsIgnoreCase("N"))
					.map(sec -> {
						EmailDTO emailDTO = new EmailDTO();
						emailDTO.setEmail(sec.getEmail());
						emailDTO.setUserCode(sec.getUserCode());
						long count = securityList.stream()
								.filter(secr -> sec.getIsEmailVerified() != null
										&& secr.getIsEmailVerified().equalsIgnoreCase("N")
										&& secr.getIsUserCodeVerified() != null
										&& secr.getIsUserCodeVerified().equalsIgnoreCase("N"))
								.count();
						emailDTO.setCount(count);
						return emailDTO;
					}).collect(Collectors.toList());
		}
		return emailInfo;
	}

	public String sendEmailtoUser(String to, String subject, String body, MultipartFile attachment) {

		String messageResp = "";
		try {
			jakarta.mail.internet.MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(body);
			helper.setFrom("your-gmail@gmail.com");
			if (attachment != null && !attachment.isEmpty()) {
				helper.addAttachment(attachment.getOriginalFilename(), new ByteArrayResource(attachment.getBytes()));
			}
			mailSender.send(message);
			messageResp = "Email sent successfully!";
		} catch (Exception e) {
			e.printStackTrace();
			messageResp = "Failed to send email: ";
		}
		return messageResp;
	}

	public List<CustomerDTO> getCustomerDetails(String email, String customerNo, String userCode) {
		List<CustomerDTO> customerList = new ArrayList<>();
		if (customerNo != null) {
			Optional<Customer> customer = customerRepo.findByCustomerNo(customerNo);
			customerList = mappingForCustomerOptional(customer);
		} else if (email != null) {
			Optional<Customer> customer = customerRepo.findByEmail(email,"N");
			customerList = mappingForCustomerOptional(customer);

		} else {
			List<Customer> customer = customerRepo.findAll();
			customerList = mappingForCustomerList(customer);
		}
		return customerList;
	}

	private List<CustomerDTO> mappingForCustomerOptional(Optional<Customer> customer) {
		List<CustomerDTO> customerList = new ArrayList<>();
		CustomerDTO custDTO = new CustomerDTO();
		if (customer != null && !customer.isEmpty()) {
			Customer cust = customer.get();
			custDTO.setAdminAccess(cust.getAdminAccess());
			custDTO.setAge(cust.getAge());
			custDTO.setEmail(cust.getEmail());
			custDTO.setGender(cust.getGender());
			custDTO.setCustomerNo(cust.getCustomerNo());
			custDTO.setMiddleName(cust.getMiddleName());
			custDTO.setName(cust.getName());
			custDTO.setPhoneNumber(cust.getPhoneNumber());
			custDTO.setSurname(cust.getSurname());
			custDTO.setUserCode(cust.getUserCode());
			custDTO.setSmokerStatus(cust.getSmokerStatus());
			custDTO.setDateOfBirth(cust.getDateOfBirth());
			// mapping for address
			Address address = new Address();
			address.setCity(cust.getCity());
			address.setCountry(cust.getCountry());
			address.setState(cust.getState());
			address.setStreet(cust.getStreet());
			address.setZipCode(cust.getZipCode());
			ContactDetails contact = new ContactDetails();
			contact.setAlternateEmail(cust.getAlternateEmail());
			contact.setEmergencyContactName(cust.getEmergencyContactName());
			contact.setEmergencyContactPhone(cust.getEmergencyContactPhone());
			contact.setPhoneCountryCode(cust.getPhoneCountryCode());
			contact.setPhoneNumber(cust.getPhoneNumber());
			custDTO.setAddress(address);
			custDTO.setContactDetails(contact);
			customerList.add(custDTO);
		}
		return customerList;
	}

	public List<CustomerDTO> mappingForCustomerList(List<Customer> customer) {
		List<CustomerDTO> customerList = customer.stream().map(cust -> {
			CustomerDTO custDTO = new CustomerDTO();
			custDTO.setAdminAccess(cust.getAdminAccess());
			custDTO.setAge(cust.getAge());
			custDTO.setEmail(cust.getEmail());
			custDTO.setGender(cust.getGender());
			custDTO.setCustomerNo(cust.getCustomerNo());
			custDTO.setMiddleName(cust.getMiddleName());
			custDTO.setName(cust.getName());
			custDTO.setPhoneNumber(cust.getPhoneNumber());
			custDTO.setSurname(cust.getSurname());
			custDTO.setUserCode(cust.getUserCode());
			custDTO.setSmokerStatus(cust.getSmokerStatus());
			custDTO.setDateOfBirth(cust.getDateOfBirth());
			// mapping for address
			Address address = new Address();
			address.setCity(cust.getCity());
			address.setCountry(cust.getCountry());
			address.setState(cust.getState());
			address.setStreet(cust.getStreet());
			address.setZipCode(cust.getZipCode());
			ContactDetails contact = new ContactDetails();
			contact.setAlternateEmail(cust.getAlternateEmail());
			contact.setEmergencyContactName(cust.getEmergencyContactName());
			contact.setEmergencyContactPhone(cust.getEmergencyContactPhone());
			contact.setPhoneCountryCode(cust.getPhoneCountryCode());
			contact.setPhoneNumber(cust.getPhoneNumber());
			custDTO.setAddress(address);
			custDTO.setContactDetails(contact);
			return custDTO;
		}).collect(Collectors.toList());
		return customerList;
	}

	@Transactional
	public String updateCustomerDetails(CustomerDTO cust, String userCode) {
		try {
			// Validate input
			if (cust == null) {
				return "Failed, customer details cannot be null";
			}
			if (cust.getName() == null || cust.getName().trim().isEmpty()) {
				return "Failed, name is required";
			}
			if (!Pattern.matches("^[A-Za-z\\s]+$", cust.getName())
					|| cust.getName().trim().toLowerCase().equals("xxxx")) {
				return "Failed, name must contain only letters and spaces, and 'xxxx' is not allowed";
			}
			if (cust.getPhoneNumber() == null || cust.getPhoneNumber().trim().isEmpty()) {
				return "Failed, phone number is required";
			}
			if (!Pattern.matches("^\\d{10}$", cust.getPhoneNumber())) {
				return "Failed, phone number must be exactly 10 digits";
			}
			if (cust.getEmail() == null || cust.getEmail().trim().isEmpty()) {
				return "Failed, email is required";
			}
			if (!Pattern.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", cust.getEmail())) {
				return "Failed, invalid email format";
			}
			if (cust.getUserCode() == null || cust.getUserCode().trim().isEmpty()) {
				return "Failed, userCode is required";
			}
			Address address = cust.getAddress();
			if (address != null && address.getZipCode() != null && !address.getZipCode().trim().isEmpty()) {
				if (!Pattern.matches("^\\d{5,6}$", address.getZipCode())) {
					return "Failed, zip code must be 5 or 6 digits";
				}
			}

			// Check for duplicate email and userCode
			Optional<Customer> existingCustomerByEmail = customerRepo.findByEmail(cust.getEmail(),"N");
			Optional<Security> existingSecurityByEmail = securityRepo.findByEmailAndDeletedFlag(cust.getEmail(), "N");
			Optional<Customer> existingCustomerByUserCode = customerRepo.findByUserCode(cust.getUserCode());
			Optional<Security> existingSecurityByUserCode = securityRepo
					.findByUserCodeAndDeletedFlag(cust.getUserCode(), "N");

			Customer existingCustomer = null;
			if (cust.getCustomerNo() != null && !cust.getCustomerNo().isEmpty()) {
				existingCustomer = customerRepo.findByCustomerNoNew(cust.getCustomerNo(),"N");
			}

			// Allow updating existing customer without duplicate error
			if (existingCustomer != null) {
				if (!existingCustomer.getEmail().equals(cust.getEmail())
						&& (existingCustomerByEmail.isPresent() || existingSecurityByEmail.isPresent())) {
					throw new DuplicateEntryException("Email '" + cust.getEmail() + "' already exists in system");
				}
				if (!existingCustomer.getUserCode().equals(cust.getUserCode())
						&& (existingCustomerByUserCode.isPresent() || existingSecurityByUserCode.isPresent())) {
					throw new DuplicateEntryException("UserCode '" + cust.getUserCode() + "' already exists in system");
				}
			} else {
				if (existingCustomerByEmail.isPresent() || existingSecurityByEmail.isPresent()) {
					throw new DuplicateEntryException("Email '" + cust.getEmail() + "' already exists in system");
				}
				if (existingCustomerByUserCode.isPresent() || existingSecurityByUserCode.isPresent()) {
					throw new DuplicateEntryException("UserCode '" + cust.getUserCode() + "' already exists in system");
				}
			}

			// Create or update Customer
			Customer custDTO = existingCustomer != null ? existingCustomer : new Customer();
			if (existingCustomer == null) {
				custDTO.setCustomerNo(generateCustomerNumber());
			}
			custDTO.setAdminAccess(cust.getAdminAccess());
			custDTO.setDeletedFlag("N");
			custDTO.setAge(cust.getAge());
			custDTO.setEmail(cust.getEmail());
			custDTO.setGender(cust.getGender());
			custDTO.setDateOfBirth(cust.getDateOfBirth());
			custDTO.setMiddleName(cust.getMiddleName());
			custDTO.setName(cust.getName());
			custDTO.setPhoneNumber(cust.getPhoneNumber());
			custDTO.setSurname(cust.getSurname());
			custDTO.setUserCode(cust.getUserCode()); // Fixed typo: getuserCode -> getUserCode
			custDTO.setSmokerStatus(cust.getSmokerStatus());
			custDTO.setCreatedBy(userCode);
			custDTO.setCreatedTime(LocalDateTime.now());

			// Map address
			if (address != null) {
				custDTO.setCity(address.getCity());
				custDTO.setCountry(address.getCountry());
				custDTO.setState(address.getState());
				custDTO.setStreet(address.getStreet());
				custDTO.setZipCode(address.getZipCode());
			}

			// Map contact details
			ContactDetails contact = cust.getContactDetails();
			if (contact != null) {
				custDTO.setAlternateEmail(contact.getAlternateEmail());
				custDTO.setEmergencyContactName(contact.getEmergencyContactName());
				custDTO.setEmergencyContactPhone(contact.getEmergencyContactPhone());
				custDTO.setPhoneCountryCode(contact.getPhoneCountryCode());
			}

			// Create or update corresponding Security record
			Optional<Security> existingSecurity = securityRepo.findByCustomerNoAndDeletedFlag(custDTO.getCustomerNo(),
					"N");
			Security security = existingSecurity.orElse(new Security());
			security.setEmail(cust.getEmail());
			security.setUserCode(cust.getUserCode());
			security.setCustomerNo(custDTO.getCustomerNo());
			security.setUserName(cust.getName() + " " + cust.getSurname());
			security.setIsEmailVerified(existingSecurity.isPresent() ? security.getIsEmailVerified() : "N");
			security.setIsUserCodeVerified(existingSecurity.isPresent() ? security.getIsUserCodeVerified() : "N");
			security.setDeletedFlag("N");
			security.setUpdateBy(userCode);
			security.setUpdateTime(LocalDate.now());
			security.setEndTime(existingSecurity.isPresent() && security.getEndTime() != null ? security.getEndTime()
					: LocalDate.now().plusDays(5));

			// Save both records
			customerRepo.save(custDTO);
			securityRepo.save(security);

			return "Success, person " + custDTO.getCustomerNo() + " created/updated successfully";
		} catch (DuplicateEntryException e) {
			return "Failed: " + e.getMessage();
		} catch (Exception e) {
			e.printStackTrace();
			return "Failed to update/create customer details: " + e.getMessage();
		}
	}

	public String generateCustomerNumber() {
		String numberFormatted = "";
		try {
			String currentYear = String.valueOf(Year.now().getValue());
			String lastCustomerNo = customerRepo.findTopCustomerNo(); // custom method
			int nextNumber = 1;
			logger.info(lastCustomerNo);
			if (lastCustomerNo != null && lastCustomerNo.startsWith("T") && lastCustomerNo.endsWith(currentYear)) {
				String numberPart = lastCustomerNo.substring(1, 7); // Extract the 6-digit number
				nextNumber = Integer.parseInt(numberPart) + 1;
			}
			numberFormatted = String.format("T%06d%s", nextNumber, currentYear);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("new generated{}", numberFormatted);
		return numberFormatted;
	}

	public boolean fingerprintLogin(String userCode) {
		try {
			Optional<Security> securityOpt = securityRepo.findByUserCodeAndDeletedFlag(userCode, "N");
			if (!securityOpt.isPresent()) {
				return false;
			}
			Security security = securityOpt.get();
			return security.getCredentialId() != null && !security.getCredentialId().isEmpty();
		} catch (NumberFormatException e) {
			return false;
		}
	}

}
