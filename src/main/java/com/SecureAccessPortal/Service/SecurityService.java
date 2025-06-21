package com.SecureAccessPortal.Service; // Use lowercase for package names

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException.BadRequest;

import com.SecureAccessPortal.CommonConstants.CommonConstant;
import com.SecureAccessPortal.Entity.Customer;
import com.SecureAccessPortal.Entity.Employees;
import com.SecureAccessPortal.Entity.LoginHistory;
import com.SecureAccessPortal.Entity.OtpStore;
import com.SecureAccessPortal.Entity.PasswordHistory;
import com.SecureAccessPortal.Entity.PersonSequence;
import com.SecureAccessPortal.Entity.Security;
import com.SecureAccessPortal.Exception.BadRequestException;
import com.SecureAccessPortal.Modal.EmpRequestforUpdate;
import com.SecureAccessPortal.Modal.EmployeeDTO;
import com.SecureAccessPortal.Modal.SecurityDTO;
import com.SecureAccessPortal.Modal.SetPasswordRequest;
import com.SecureAccessPortal.Modal.WorkItemDTO;
import com.SecureAccessPortal.Repo.CustomerRepo;
import com.SecureAccessPortal.Repo.IEmployeeRepo;
import com.SecureAccessPortal.Repo.ISecurityRepo;
import com.SecureAccessPortal.Repo.LoginHistoryRepo;
import com.SecureAccessPortal.Repo.OtpStoreRepo;
import com.SecureAccessPortal.Repo.PasswordHistoryRepo;
import com.SecureAccessPortal.Repo.PersonSequenceRepository;
import com.SecureAccessPortal.Transformer.IEmployeeMapper;
import com.SecureAccessPortal.Transformer.SecurityMapper;
import com.SecureAccessPortal.util.DateUtil;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

@Service
public class SecurityService implements ISecrityService {

	@Autowired
	private IEmployeeRepo employeeRepo;

	@Autowired
	private IEmployeeMapper employeeMapper;

	@Autowired
	private ISecurityRepo securityRepository;

	@Autowired
	private SecurityMapper securityMapper;

	@Autowired
	private IWorkItemService workItemService;

	@Autowired
	private PersonSequenceRepository personSequenceRepository;

	@Autowired
	private EmailService otpService;

	@Autowired
	private CustomerRepo customerRepo;

	@Autowired
	private DateUtil dateUtil;

	@Autowired
	private OtpStoreRepo otpStoreRepository;

	@Autowired
	private PasswordHistoryRepo passwordHistoryRepository;
	
	@Autowired
	private LoginHistoryRepo loginHistoryRepository;
	
    Logger logger = LoggerFactory.getLogger(this.getClass());


	@Override
	public List<EmployeeDTO> fetchEmpList(String id, String userCode) {
		// Convert the String id to Long (assuming id is a numeric string)
		List<EmployeeDTO> employeeList = new ArrayList<>();
		EmployeeDTO employeeDTO = new EmployeeDTO();

		// Long employeeId = Long.parseLong(id);
		try {
			// for Security
			List<Security> security = securityRepository.findAll();
			boolean Notfound = security.stream().anyMatch(user -> user.getUserCode().equalsIgnoreCase(userCode));

			if (!Notfound) {
				throw new BadRequestException("Invalid User" + userCode);
			}

		} catch (BadRequest e) {
			e.getMessage();
		}
		try {
			Optional<Employees> optionalEmployee = employeeRepo.findById(id);
			if (optionalEmployee.isPresent()) {
				Employees employee = optionalEmployee.get();
				employeeDTO = employeeMapper.convertToDTO(employee);
				employeeList.add(employeeDTO);
			}
		} catch (Exception e) {
			e.getCause();
		}
		return employeeList;
	}

	@Override
	public EmployeeDTO updateEmployee(EmpRequestforUpdate employeeRequest, String userCode) {
		EmployeeDTO employeeDTO = new EmployeeDTO();
		try {
			// for Security
			List<Security> security = securityRepository.findAll();
			boolean Notfound = security.stream().anyMatch(user -> user.getUserCode().equalsIgnoreCase(userCode));

			if (!Notfound) {
				throw new BadRequestException("Invalid User" + userCode);
			}

		} catch (BadRequest e) {
			e.getMessage();
		}
		if (employeeRequest.getId() != null) {
			Optional<Employees> isExisting = employeeRepo.findById(employeeRequest.getId());
			// toUpdateExisting
			if (isExisting.isPresent()) {
				Employees emplo = isExisting.get();

				Employees employee = employeeMapper.mapEmployeeRequest(employeeRequest, String.valueOf(emplo.getId()));
				Employees empSave = employeeRepo.save(employee);
				employeeDTO = employeeMapper.convertToDTO(empSave);
				// Create WorkItem whenever added new Employee or update.
				WorkItemDTO workItemRequest = new WorkItemDTO();
				workItemRequest.setComment("WorkItem getting created for Update Employee Details");
				workItemRequest.setCreatedBy(userCode);
				workItemRequest.setWorkType(CommonConstant.ADD_NEW_EMPLOYEE);
				WorkItemDTO workItem = workItemService.createWorkItem(workItemRequest, userCode);

				// Implement Email API. to Share Info.
				if (employeeRequest.getEmail() != null) {
					String response = otpService.sendDetailEmail(employeeRequest.getEmail(), emplo.getId(), workItem,
							userCode);
				}

			}
		} else {
			String id = generateCustomerNumber();
			Employees employee = employeeMapper.mapEmployeeRequest(employeeRequest, id);
			Employees empSave = employeeRepo.save(employee);
			employeeDTO = employeeMapper.convertToDTO(empSave);
			// Create WorkItem whenever added new Employee or update.
			WorkItemDTO workItemRequest = new WorkItemDTO();
			workItemRequest.setComment("WorkItem getting created for new employee");
			workItemRequest.setCreatedBy(userCode);
			workItemRequest.setWorkType(CommonConstant.ADD_NEW_EMPLOYEE);
			WorkItemDTO workItem = workItemService.createWorkItem(workItemRequest, userCode);

			// Implement Email API. to Share Info.
			if (employeeRequest.getEmail() != null) {
				String response = otpService.sendDetailEmail(employeeRequest.getEmail(), id, workItem, userCode);
			}
		}

		return employeeDTO;
	}

	@Override
	public List<EmployeeDTO> fetchAllEmployee(String userCode) {
		try {
			// for Security
			List<Security> security = securityRepository.findAll();
			boolean Notfound = security.stream().anyMatch(user -> user.getUserCode().equalsIgnoreCase(userCode));

			if (!Notfound) {
				throw new BadRequestException("Invalid User" + userCode);
			}

		} catch (BadRequest e) {
			e.getMessage();
		}
		List<Employees> employee = employeeRepo.findAll();
		List<EmployeeDTO> employDTO = employeeMapper.mapAllEmployee(employee);
		return employDTO;
	}

	@Override
	public byte[] fetchEmployeeDBforPDF(String id, String userCode) throws IOException {
		final String DIRECTORY = "D:/Employee_PDFs/";
		Employees employee = new Employees();
		List<Employees> employeesList = new ArrayList<>();
		// Ensure directory exists
		Files.createDirectories(Paths.get(DIRECTORY));

		try {
			// for Security
			List<Security> security = securityRepository.findAll();
			boolean Notfound = security.stream().anyMatch(user -> user.getUserCode().equalsIgnoreCase(userCode));

			if (!Notfound) {
				throw new BadRequestException("Invalid User" + userCode);
			}

		} catch (BadRequest e) {
			e.getMessage();
		}

		// Fetch employee details from DB
		if (id == null) {
			employeesList = employeeRepo.findAll();
			id = "100";

		}

		else {
			Optional<Employees> empFound = employeeRepo.findById(id);
			if (empFound.isEmpty()) {
				throw new RuntimeException("Employee not found for ID: " + id);
			}
			employee = empFound.get();
		}

		// Define file path
		String filePath = DIRECTORY + "employee_" + id + ".pdf";

		try {
			// **Create PDF File**
			PdfWriter writer = new PdfWriter(filePath);
			PdfDocument pdf = new PdfDocument(writer);
			Document document = new Document(pdf);

			if (!employeesList.isEmpty()) {
				employeesList.stream().forEach(employees -> {
					document.add(new Paragraph("Employee Details"));
					document.add(new Paragraph("ID: " + employees.getId()));
					document.add(new Paragraph("Name: " + employees.getFirstName()));
					document.add(new Paragraph("Designation: " + employees.getLastName()));
					document.add(new Paragraph("Department: " + employees.getDepartment()));
					document.add(new Paragraph("Email: " + employees.getEmail()));
				});
			}

			else {
				document.add(new Paragraph("Employee Details"));
				document.add(new Paragraph("ID: " + employee.getId()));
				document.add(new Paragraph("Name: " + employee.getFirstName()));
				document.add(new Paragraph("Designation: " + employee.getLastName()));
				document.add(new Paragraph("Department: " + employee.getDepartment()));
				document.add(new Paragraph("Email: " + employee.getEmail()));
			}
			document.close(); // Ensure the document is properly closed
			writer.close();

		} catch (Exception e) {
			e.printStackTrace();
			throw new IOException("Error while generating PDF", e);
		}

		// **Check if File Exists**
		File file = new File(filePath);
		if (!file.exists()) {
			throw new FileNotFoundException("PDF file was not created at: " + filePath);
		}

		System.out.println("PDF successfully created at: " + filePath);

		// **Read and Return the PDF Bytes**
		return Files.readAllBytes(Paths.get(filePath));
	}

	@Override
	public List<SecurityDTO> fetchListOfUsers(String id, String userCode) {
		List<SecurityDTO> allUsers = List.of();
		List<Security> securityList = new ArrayList<>();
		try {
			if (id != null) {
				Security security = securityRepository.findById(id, "N");
				securityList.add(security);
			} else {
				securityList = securityRepository.findAll("N");
			}
			allUsers = securityMapper.mapSecurity(securityList, userCode);
		} catch (Exception e) {
			e.getCause();
		}
		return allUsers;
	}

	@Override
	@Transactional
	public com.SecureAccessPortal.Service.ResponseEntity<SecurityDTO> createUser(SecurityDTO securityDTO,
			String userCode) {
		com.SecureAccessPortal.Service.ResponseEntity<SecurityDTO> response = new com.SecureAccessPortal.Service.ResponseEntity<>();
		List<String> errors = new ArrayList<>();

		// Validate input
		if (securityDTO.getEmail() == null || securityDTO.getEmail().trim().isEmpty()) {
			errors.add("Email is required");
		}
		if (securityDTO.getUserCode() == null || securityDTO.getUserCode().trim().isEmpty()) {
			errors.add("UserCode is required");
		}
		if (!errors.isEmpty()) {
			response.setErrorMessage(String.join("; ", errors));
			return response;
		}
		// Check for existing Security and Customer records
		Optional<Security> existingSecurityByEmail = securityRepository
				.findByEmailAndDeletedFlag(securityDTO.getEmail(), "N");
		Optional<Customer> existingCustomerByEmail = customerRepo.findByEmail(securityDTO.getEmail());
		Optional<Security> existingSecurityByUserCode = securityRepository
				.findByUserCodeAndDeletedFlag(securityDTO.getUserCode(), "N");
		Optional<Customer> existingCustomerByUserCode = customerRepo.findByUserCode(securityDTO.getUserCode());

		Security securityEntity;
		Customer customer = null;
		if (existingSecurityByEmail.isPresent()) {
			// Update existing Security record
			securityEntity = existingSecurityByEmail.get();
			// Verify userCode matches to prevent updating another user's record
			if (!securityEntity.getUserCode().equals(securityDTO.getUserCode())) {
				response.setErrorMessage(
						"Email '" + securityDTO.getEmail() + "' is associated with a different userCode");
				return response;
			}
			// Update fields
			securityEntity.setUserName(securityDTO.getUserName());
			securityEntity
					.setIsEmailVerified(securityDTO.getIsEmailVerified() != null ? securityDTO.getIsEmailVerified()
							: securityEntity.getIsEmailVerified());
			securityEntity.setIsUserCodeVerified(
					securityDTO.getIsUserCodeVerified() != null ? securityDTO.getIsUserCodeVerified()
							: securityEntity.getIsUserCodeVerified());
			securityEntity.setUpdateBy(userCode);
			securityEntity.setUpdateTime(LocalDate.now());

			if (securityDTO.getRemainingTime() != null) {
				LocalDate remainTime = dateUtil.stringToLocalDateConvert(securityDTO.getRemainingTime());
				securityEntity.setEndTime(remainTime.plusDays(5));
			}

			// Update WebAuthn credentials if provided
			if (securityDTO.getCredentialId() != null) {
				securityEntity.setCredentialId(securityDTO.getCredentialId());
				securityEntity.setPublicKey(securityDTO.getPublicKey());
				securityEntity.setUserHandle(securityDTO.getUserHandle());
				securityEntity.setSignatureCounter(securityDTO.getSignatureCounter());
			}

			// No changes to Customer record
		} else if (existingCustomerByEmail.isPresent() || existingSecurityByUserCode.isPresent()
				|| existingCustomerByUserCode.isPresent()) {
			// Duplicate exists for email or userCode in Customer or different Security
			// record
			if (existingCustomerByEmail.isPresent()) {
				response.setErrorMessage("Email '" + securityDTO.getEmail() + "' already exists in Customer table");
			} else if (existingSecurityByUserCode.isPresent()) {
				response.setErrorMessage(
						"UserCode '" + securityDTO.getUserCode() + "' already exists in Security table");
			} else {
				response.setErrorMessage(
						"UserCode '" + securityDTO.getUserCode() + "' already exists in Customer table");
			}
			return response;
		} else {
			// Create new Security and Customer records
			securityEntity = new Security();
			securityEntity.setEmail(securityDTO.getEmail());
			securityEntity.setUserCode(securityDTO.getUserCode());
			securityEntity.setUserName(securityDTO.getUserName());
			securityEntity.setIsEmailVerified(
					securityDTO.getIsEmailVerified() != null ? securityDTO.getIsEmailVerified() : "N");
			securityEntity.setIsUserCodeVerified(
					securityDTO.getIsUserCodeVerified() != null ? securityDTO.getIsUserCodeVerified() : "N");
			securityEntity.setDeletedFlag("N");
			securityEntity.setUpdateBy(userCode);
			securityEntity.setUpdateTime(LocalDate.now());

			if (securityDTO.getRemainingTime() != null) {
				LocalDate remainTime = dateUtil.stringToLocalDateConvert(securityDTO.getRemainingTime());
				securityEntity.setEndTime(remainTime.plusDays(5));
			} else {
				securityEntity.setEndTime(LocalDate.now().plusDays(5));
			}

			// Set WebAuthn credentials if provided
			if (securityDTO.getCredentialId() != null) {
				securityEntity.setCredentialId(securityDTO.getCredentialId());
				securityEntity.setPublicKey(securityDTO.getPublicKey());
				securityEntity.setUserHandle(securityDTO.getUserHandle());
				securityEntity.setSignatureCounter(securityDTO.getSignatureCounter());
			}

			// Create corresponding Customer record
			customer = new Customer();
			String customerNo = generateCustomerNumber();
			customer.setCustomerNo(customerNo);
			customer.setEmail(securityDTO.getEmail());
			customer.setUserCode(securityDTO.getUserCode());
			customer.setCreatedBy(userCode);
			customer.setCreatedTime(LocalDateTime.now());

			// Link Security to Customer
			securityEntity.setCustomerNo(customerNo);
		}

		// Save records
		if (customer != null) {
			customerRepo.save(customer);
		}
		securityRepository.save(securityEntity);

		response.setData(securityDTO);
		return response;
	}

	public String generateCustomerNumber() {
		PersonSequence seq = personSequenceRepository.save(new PersonSequence());
		Long nextVal = seq.getId();
		return "T" + String.format("%09d", nextVal);
	}

	@Transactional
	public com.SecureAccessPortal.Service.ResponseEntity<SecurityDTO> registerUser(SecurityDTO securityDTO,
			String userCode) {
		ResponseEntity<SecurityDTO> response = new ResponseEntity<>();
		List<String> errors = new ArrayList<>();

		// Validate input
		if (securityDTO.getEmail() == null || securityDTO.getEmail().trim().isEmpty()) {
			errors.add("Email is required");
		}
		if (securityDTO.getUserCode() == null || securityDTO.getUserCode().trim().isEmpty()) {
			errors.add("UserCode is required");
		}
		if (!errors.isEmpty()) {
			response.setErrorMessage(String.join("; ", errors));
			return response;
		}

		// Check for existing Security and Customer records
		Optional<Security> existingSecurityByEmail = securityRepository
				.findByEmailAndDeletedFlag(securityDTO.getEmail(), "N");
		Optional<Customer> existingCustomerByEmail = customerRepo.findByEmail(securityDTO.getEmail());
		Optional<Security> existingSecurityByUserCode = securityRepository
				.findByUserCodeAndDeletedFlag(securityDTO.getUserCode(), "N");
		Optional<Customer> existingCustomerByUserCode = customerRepo.findByUserCode(securityDTO.getUserCode());

		if (existingSecurityByEmail.isPresent()) {
			Security security = existingSecurityByEmail.get();
			// Check if userCode matches
			if (!security.getUserCode().equalsIgnoreCase(securityDTO.getUserCode())) {
				response.setErrorMessage(
						"Email '" + securityDTO.getEmail() + "' is associated with a different userCode");
				return response;
			}
			// Check verification status
			if ("Y".equals(security.getIsEmailVerified()) && "Y".equals(security.getIsUserCodeVerified())) {
				response.setErrorMessage("User already exists in system and verified. Please go to login page");
				return response;
			} else if ("N".equals(security.getIsEmailVerified()) && "N".equals(security.getIsUserCodeVerified())) {
				response.setErrorMessage("User already exists in system and pending for verification");
				return response;
			} else {
				response.setErrorMessage("User exists with partial verification. Please complete verification");
				return response;
			}
		} else if (existingCustomerByEmail.isPresent()) {
			response.setErrorMessage("Email '" + securityDTO.getEmail() + "' already exists in Customer table");
			return response;
		} else if (existingSecurityByUserCode.isPresent()) {
			response.setErrorMessage(
					"UserCode '" + securityDTO.getUserCode() + "' already exists in Security table,please check email");
			return response;
		} else if (existingCustomerByUserCode.isPresent()) {
			response.setErrorMessage(
					"UserCode '" + securityDTO.getUserCode() + "' already exists in Customer table,please check email");
			return response;
		}

		// Create new Security record
		Security securityEntity = new Security();
		securityEntity.setEmail(securityDTO.getEmail());
		securityEntity.setUserCode(securityDTO.getUserCode());
		securityEntity.setIsEmailVerified("N");
		securityEntity.setIsUserCodeVerified("N");
		securityEntity.setDeletedFlag("N");
		securityEntity.setUpdateBy(userCode);
		securityEntity.setUpdateTime(LocalDate.now());
		securityEntity.setEndTime(LocalDate.now().plusDays(5));

		// Create corresponding Customer record
		Customer customer = new Customer();
		String customerNo = generateCustomerNumber();
		customer.setCustomerNo(customerNo);
		customer.setEmail(securityDTO.getEmail());
		customer.setUserCode(securityDTO.getUserCode());
		customer.setCreatedBy(userCode);
		customer.setCreatedTime(LocalDateTime.now());
		securityEntity.setCustomerNo(customerNo);
		customerRepo.save(customer);
		securityRepository.save(securityEntity);
		response.setData(securityDTO);
		response.setStatus("Successfully registered, pending for verification");
		return response;
	}

	@Override
	public boolean deleteNoteById(Long id) {
		if (id != null) {
			securityRepository.deleteById(id);
			return true;
		}
		return false;
	}

	@Override
	public ResponseEntity<SecurityDTO> searchUserFromList(SecurityDTO searchRequest, String userCode) {
		ResponseEntity<SecurityDTO> serchResp = new ResponseEntity<>();

		SecurityDTO securityDTO = new SecurityDTO();

		if (searchRequest.getUserName() != null) {
			Security user = securityRepository.findByUserName(searchRequest.getUserName(), "N");
			if (user != null) {
				securityDTO.setEmail(user.getEmail());
				securityDTO.setUserCode(user.getUserCode());
				serchResp.setData(securityDTO);
			} else {
				serchResp.setErrorMessage("No user found");
			}
		} else if (searchRequest.getEmail() != null) {
			Security user = securityRepository.findByEmail(searchRequest.getEmail(), "N");
			if (user != null) {
				securityDTO.setEmail(user.getEmail());
				securityDTO.setUserCode(user.getUserCode());
				serchResp.setData(securityDTO);
			} else {
				serchResp.setErrorMessage("No user found");
			}
		} else if (searchRequest.getUserCode() != null) {
			Security user = securityRepository.findByUserCodeDeletedN(searchRequest.getUserCode(), "N");
			if (user != null) {
				securityDTO.setEmail(user.getEmail());
				securityDTO.setUserCode(user.getUserCode());
				serchResp.setData(securityDTO);
			} else {
				serchResp.setErrorMessage("No user found");
			}
		}
		return serchResp;
	}

	public com.SecureAccessPortal.Service.ResponseEntity<String> registerWebAuthnCredentials(SecurityDTO request) {
		com.SecureAccessPortal.Service.ResponseEntity<String> response = new ResponseEntity<String>();
		String message = "";
		if (request.getUserCode() == null) {
			throw new IllegalArgumentException("User ID is mandatory");
		}
		Optional<Security> existingSecurityByUserCode = securityRepository
				.findByUserCodeAndDeletedFlag(request.getUserCode(), "N");
		if (!existingSecurityByUserCode.isPresent()) {
			response.setErrorMessage("UserCode '" + request.getUserCode() + "is not registerted in System");
			return response;
		} else {
			Security security = existingSecurityByUserCode.get();
			security.setCredentialId(request.getCredentialId());
			security.setPublicKey(request.getPublicKey());
			security.setUserHandle(request.getUserHandle());
			security.setSignatureCounter(request.getSignatureCounter());
			security.setUpdateTime(LocalDate.now());
			security.setUpdateBy(request.getUserCode());
			security.setDeletedFlag("N");
			securityRepository.save(security);
			message = "Success, fngerprint added successfully";
			response.setStatus(message);
		}
		return response;
	}

	@Override
	public ResponseEntity<String> setPassword(SetPasswordRequest request, String userCode) {

		ResponseEntity<String> response = new ResponseEntity<String>();
		try {
			Optional<Security> securityOpt = securityRepository.findByEmailOrUserCodeAndDeletedFlag(
					request.getEmail() != null ? request.getEmail() : "",
					request.getUserCode() != null ? request.getUserCode() : "", "N");
			if (securityOpt.isEmpty()) {
				response.setErrorMessage("User not found or deleted.");
				return response;
			}
			Security security = securityOpt.get();
			if (!"Y".equals(security.getIsEmailVerified()) || !"Y".equals(security.getIsUserCodeVerified())) {
				response.setErrorMessage("Account not verified.");
				return response;
			}

			// Validate OTP
			Optional<OtpStore> otpRecord = otpStoreRepository.findByEmailAndOtpAndDeletedFlag(security.getEmail(),
					request.getOtp(), "N");
			if (otpRecord.isEmpty() || otpRecord.get().getCreatedTime().isAfter(LocalDateTime.now())
					|| otpRecord.get().getExpiryTime().isBefore(LocalDateTime.now())) {
				response.setErrorMessage("Invalid or expired OTP.");
				return response;
			}

			// Mark OTP as used
//        OtpStore otp = otpRecord.get();
//        otp.setDeletedFlag("Y");
//        otpStoreRepository.save(otp);

			// Validate password and confirmPassword
			if (!request.getPassword().equals(request.getConfirmPassword())) {
				response.setErrorMessage("Passwords do not match.");
				return response;
			}

			// Check last 5 passwords
			List<PasswordHistory> passwordHistory = passwordHistoryRepository
					.findTop5ByEmailAndIsCurrentFalseAndDeletedFlagOrderByCreatedTimeDesc(security.getEmail(), "N");
			String newPasswordHash = BCrypt.hashpw(request.getPassword(), BCrypt.gensalt(12));
			for (PasswordHistory history : passwordHistory) {
				if (BCrypt.checkpw(request.getPassword(), history.getHashedPassword())) {
					response.setErrorMessage("Cannot reuse one of your last 5 passwords.");
				}
			}

			// Update existing current password
			passwordHistoryRepository.findByEmailAndIsCurrentTrueAndDeletedFlag(security.getEmail(), "N")
					.ifPresent(current -> {
						current.setCurrent(false);
						current.setUpdatedBy(request.getUserCode() != null ? request.getUserCode() : "system");
						current.setUpdatedTime(Timestamp.valueOf(LocalDateTime.now()));
						passwordHistoryRepository.save(current);
					});

			// Save new password
			PasswordHistory newPassword = new PasswordHistory();
			newPassword.setSecurity(security);
			// newPassword.setEmail(security.getEmail());
			newPassword.setUserCode(security.getUserCode());
			newPassword.setHashedPassword(newPasswordHash);
			newPassword.setCurrent(true);
			newPassword.setExpireTime(
					Timestamp.from(LocalDateTime.now().plusDays(30).atZone(ZoneId.systemDefault()).toInstant()));
			newPassword.setCreatedTime(Timestamp.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
			newPassword.setCreatedBy(request.getUserCode() != null ? request.getUserCode() : "system");
			newPassword.setUpdatedBy(request.getUserCode() != null ? request.getUserCode() : "system");
			newPassword.setUpdatedTime(Timestamp.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
			newPassword.setPasswordChangeReason("otp-verified");
			newPassword.setFailedLoginCount(0);
			newPassword.setMfaEnabled(false);
			newPassword.setBreachStatus(false);
			newPassword.setDeletedFlag("N");
			passwordHistoryRepository.save(newPassword);

			// Maintain only 5 historical passwords
			List<PasswordHistory> oldPasswords = passwordHistoryRepository
					.findByEmailAndIsCurrentFalseAndDeletedFlagOrderByCreatedTimeAsc(security.getEmail(), "N");
			if (oldPasswords.size() > 5) {
				oldPasswords.get(0).setDeletedFlag("Y");
				passwordHistoryRepository.save(oldPasswords.get(0));
			}

			response.setStatus("Password set successfully.");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@Override
	public ResponseEntity<String> loginUsingPassword(String email, String userCode, String password) {

		ResponseEntity<String> response = new ResponseEntity<>();

		// Validate input: at least one of email or userCode is required
		if ((email == null || email.trim().isEmpty()) && (userCode == null || userCode.trim().isEmpty())) {
			response.setStatus("Error");
			response.setErrorMessage("Email or UserCode is required.");
			logger.warn("Login attempt failed: Email and UserCode are null or empty.");
			return response;
		}
		if (password == null || password.trim().isEmpty()) {
			response.setStatus("Error");
			response.setErrorMessage("Password is required.");
			logger.warn("Login attempt failed: Password is null or empty.");
			return response;
		}
		try {
			Security security = null;
			String lookupField = email != null && !email.trim().isEmpty() ? "email" : "userCode";
			String lookupValue = lookupField.equals("email") ? email.trim().toLowerCase() : userCode.trim();

			if (lookupField.equals("email")) {
				security = securityRepository.findByEmailAndDeletedFlag(lookupValue, "N").orElse(null);
			} else {
				security = securityRepository.findByUserCodeAndDeletedFlag(lookupValue, "N").orElse(null);
			}
			if (security == null) {
				response.setStatus("Error");
				response.setErrorMessage(lookupField.equals("email") ? "Email not found." : "UserCode not found.");
				logLoginAttempt(email, userCode, false,
						lookupField.equals("email") ? "Email not found" : "UserCode not found");
				logger.warn("Login attempt failed for {}: {}. Not found.", lookupField, lookupValue);
				return response;
			}
			if (!"Y".equals(security.getIsEmailVerified()) || !"Y".equals(security.getIsUserCodeVerified())) {
				response.setStatus("Error");
				response.setErrorMessage("Account not verified. Please contact admin.");
				logLoginAttempt(email, userCode, false, "Account not verified");
				logger.warn("Login attempt failed for {}: {}. Account not verified.", lookupField, lookupValue);
				return response;
			}

			PasswordHistory passwordHistory = passwordHistoryRepository.findByEmailAndIsCurrentTrueAndDeletedFlag(security.getEmail(), "N").orElse(null);
	        if (passwordHistory == null) {
	            response.setStatus("Error");
	            response.setErrorMessage("No current password found for user.");
	            logLoginAttempt(email, userCode, false, "No current password found");
	            logger.warn("Login attempt failed for {}: {}. No current password found.", lookupField, lookupValue);
	            return response;
	        }

	        // Verify password using BCrypt.checkpw
	        if (!BCrypt.checkpw(password, passwordHistory.getHashedPassword())) {
	            response.setStatus("passwordnotmatch");
	            response.setErrorMessage("Invalid password.");
	            logLoginAttempt(email, userCode, false, "Password does not match");
	            logger.warn("Login attempt failed for {}: {}. Invalid password.", lookupField, lookupValue);
	            return response;
	        }
			else {
				response.setStatus("Password Match Successfully for "+ passwordHistory.getUserCode());
				logLoginAttempt(email, userCode, true, "Password Match");
			}
		} catch (Exception e) {
			response.setStatus("Error");
			response.setErrorMessage("Login failed: " + e.getMessage());
			logLoginAttempt(email, userCode, false, "Exception: " + e.getMessage());
			logger.error("Exception during password login for email: {}, userCode: {}. Error: {}", email, userCode,
					e.getMessage(), e);
		}

		return response;
	}

	private void logLoginAttempt(String email, String userCode, boolean success, String reason) {
		try {
			LoginHistory loginHistory = new LoginHistory();
			//loginHistory.setId(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
			Optional<Security> existingSecurityByEmail = securityRepository.findByEmailAndDeletedFlag(email, "N");
			Security security = existingSecurityByEmail.get();
			loginHistory.setSecurity(security);
			loginHistory.setUserCode(security.getUserCode());
			loginHistory.setLoginTime(Timestamp.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
			
			Timestamp loginTimestamp = Timestamp.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
			if (loginTimestamp != null) {
				LocalDateTime loginTime = loginTimestamp.toLocalDateTime();
				LocalDateTime logoutTime = loginTime.plusMinutes(30);
				loginHistory.setLogoutTime(Timestamp.valueOf(logoutTime));
			}
			loginHistory.setSuccess(success);
			loginHistory.setIpAddress("0.0.0.0"); // Placeholder; use actual IP in production
			loginHistory.setDeviceInfo("Unknown"); // Placeholder; capture from request
			loginHistory.setSessionId(UUID.randomUUID().toString());
			loginHistory.setLoginMethod("Password");
			loginHistory.setRiskScore(0); // Placeholder; calculate based on logic
			loginHistory.setDeletedFlag("N");
			loginHistory.setCreatedBy(security.getUserCode());
			loginHistory.setCreatedTime(Timestamp.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
			loginHistory.setUpdatedBy(security.getUserCode());
			loginHistory.setUpdatedTime(Timestamp.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
			loginHistory.setMfaUsed(false);
			loginHistoryRepository.save(loginHistory);
			logger.info("Login attempt logged: email={}, userCode={}, success={}, reason={}", email, userCode, success,
					reason);
		} catch (Exception e) {
			logger.error("Failed to log login attempt for email: {}, userCode: {}. Error: {}", email, userCode,
					e.getMessage(), e);
		}
	}
}
