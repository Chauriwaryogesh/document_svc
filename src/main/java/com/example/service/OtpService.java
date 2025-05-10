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
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ws.mime.MimeMessage;

import com.example.CommonConstants.CommonConstant;
import com.example.dto.EmailDTO;
import com.example.dto.WorkItemDTO;
import com.example.entity.Email;
import com.example.entity.OtpStore;
import com.example.entity.Security;
import com.example.repo.IEmailRepo;
import com.example.repo.IOtpServiceDB;
import com.example.repo.ISecurityRepo;

import jakarta.mail.internet.InternetAddress;

@Service
public class OtpService {
	private static final Logger log = LoggerFactory.getLogger(OtpService.class);

	@Autowired
	private IEmailRepo emailRepo;
	
	@Autowired
	private IWorkItemService workItemService;
	
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
		String response= "";
		String otp = generateOtp();
		otpStore.put(email, otp);

		email = verifyEmail(email);

		try {
			//check email Status is Verified Y 
			
			Security security = securityRepo.findByEmail(email,"N");
			if(security != null) {
				if (security.getEmail().equalsIgnoreCase(email) && security.getUserCode().equalsIgnoreCase(userId)
						&& security.getIsEmailVerified().equals("Y") && security.getIsUserCodeVerified().equals("Y")) {

					jakarta.mail.internet.MimeMessage message = mailSender.createMimeMessage();
					MimeMessageHelper helper = new MimeMessageHelper(message, false, "utf-8");
					helper.setFrom(new InternetAddress("KIoSK_Bank_Helpline@gmail.com", "IneternetBanking_OTP"));
					helper.setTo(email);
					helper.setSubject("Your one time password for Secure Login ");
					helper.setText("Your otp is " + otp + " Expire after 1 hrs.");
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
					response="Success,Otp send SuccessFully";		
				}else {
					response="Email is Not verified please connect with admin";
				}
			}else {
				response="Email is Not Registered in System please connect with admin";
			}
			} catch (Exception e) {
			e.printStackTrace();
			response="Otp send faild";
		}
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
				log.info("WorkItemcreated succesfully");
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
	

}
