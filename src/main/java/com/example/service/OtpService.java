package com.example.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.example.entity.OtpStore;
import com.example.repo.IOtpServiceDB;

import jakarta.mail.internet.InternetAddress;

@Service
public class OtpService {

	private final IOtpServiceDB IOtpServiceDB;

	private final JavaMailSender mailSender;

	private final Map<String, String> otpStore = new HashMap<>();

	private static final AtomicInteger counter = new AtomicInteger(1);

	public OtpService(JavaMailSender mailSender, IOtpServiceDB IOtpServiceDB) {
		this.mailSender = mailSender;
		this.IOtpServiceDB = IOtpServiceDB;
	}

	public String sendOtp(String email, String userId) {
		String otp = generateOtp();
		otpStore.put(email, otp);
		
		email =verifyEmail(email);
		
		try {
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Otp send SuccessFully";
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
		OtpStore otpStore = emailData.get();
		LocalDateTime dateTime = LocalDateTime.now();
		Long epochMillis = dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
		
		boolean found = emailData.stream().filter(data -> data.getEmail().equalsIgnoreCase(email)
				&& data.getOtp().equalsIgnoreCase(otp) && epochMillis <= data.getExpiryTime()).findAny().isPresent();

		return found;
	}

	public long nextcount() {
		return counter.getAndIncrement();
	}

}
