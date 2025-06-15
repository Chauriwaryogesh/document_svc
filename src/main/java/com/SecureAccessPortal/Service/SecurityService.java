package com.SecureAccessPortal.Service; // Use lowercase for package names

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException.BadRequest;

import com.SecureAccessPortal.CommonConstants.CommonConstant;
import com.SecureAccessPortal.Entity.Customer;
import com.SecureAccessPortal.Entity.Employees;
import com.SecureAccessPortal.Entity.PersonSequence;
import com.SecureAccessPortal.Entity.Security;
import com.SecureAccessPortal.Exception.BadRequestException;
import com.SecureAccessPortal.Exception.DuplicateEntryException;
import com.SecureAccessPortal.Modal.EmpRequestforUpdate;
import com.SecureAccessPortal.Modal.EmployeeDTO;
import com.SecureAccessPortal.Modal.SecurityDTO;
import com.SecureAccessPortal.Modal.WorkItemDTO;
import com.SecureAccessPortal.Repo.CustomerRepo;
import com.SecureAccessPortal.Repo.IEmployeeRepo;
import com.SecureAccessPortal.Repo.ISecurityRepo;
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
	private ISecurityRepo securityRepo;

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

	@Override
	public List<EmployeeDTO> fetchEmpList(String id, String userCode) {
		// Convert the String id to Long (assuming id is a numeric string)
		List<EmployeeDTO> employeeList = new ArrayList<>();
		EmployeeDTO employeeDTO = new EmployeeDTO();

		// Long employeeId = Long.parseLong(id);
		try {
			// for Security
			List<Security> security = securityRepo.findAll();
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
			List<Security> security = securityRepo.findAll();
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
			List<Security> security = securityRepo.findAll();
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
			List<Security> security = securityRepo.findAll();
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
	public List<SecurityDTO> fetchListOfUsers(String id, String userCode){
		List<SecurityDTO> allUsers = List.of();
		List<Security> securityList =new ArrayList<>();
		try { 
			if(id != null) {
				Security security = securityRepo.findById(id, "N");
				securityList.add(security);
			}else {
				 securityList = securityRepo.findAll("N");
			}
			allUsers = securityMapper.mapSecurity(securityList,userCode);
		} catch (Exception e) {
			e.getCause();
		}
		return allUsers;
	}
	@Override
    @Transactional
    public com.SecureAccessPortal.Service.ResponseEntity<SecurityDTO> createUser(SecurityDTO securityDTO, String userCode) {
		com.SecureAccessPortal.Service.ResponseEntity<SecurityDTO> response= new com.SecureAccessPortal.Service.ResponseEntity<>();
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
	        Optional<Security> existingSecurityByEmail = securityRepo.findByEmailAndDeletedFlag(securityDTO.getEmail(), "N");
	        Optional<Customer> existingCustomerByEmail = customerRepo.findByEmail(securityDTO.getEmail());
	        Optional<Security> existingSecurityByUserCode = securityRepo.findByUserCodeAndDeletedFlag(securityDTO.getUserCode(), "N");
	        Optional<Customer> existingCustomerByUserCode = customerRepo.findByUserCode(securityDTO.getUserCode());

	        Security securityEntity;
	        Customer customer = null;
	        if (existingSecurityByEmail.isPresent()) {
	            // Update existing Security record
	            securityEntity = existingSecurityByEmail.get();
	            // Verify userCode matches to prevent updating another user's record
	            if (!securityEntity.getUserCode().equals(securityDTO.getUserCode())) {
	                response.setErrorMessage("Email '" + securityDTO.getEmail() + "' is associated with a different userCode");
	                return response;
	            }
	            // Update fields
	            securityEntity.setUserName(securityDTO.getUserName());
	            securityEntity.setIsEmailVerified(securityDTO.getIsEmailVerified() != null ? securityDTO.getIsEmailVerified() : securityEntity.getIsEmailVerified());
	            securityEntity.setIsUserCodeVerified(securityDTO.getIsUserCodeVerified() != null ? securityDTO.getIsUserCodeVerified() : securityEntity.getIsUserCodeVerified());
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
	        } else if (existingCustomerByEmail.isPresent() || existingSecurityByUserCode.isPresent() || existingCustomerByUserCode.isPresent()) {
	            // Duplicate exists for email or userCode in Customer or different Security record
	            if (existingCustomerByEmail.isPresent()) {
	                response.setErrorMessage("Email '" + securityDTO.getEmail() + "' already exists in Customer table");
	            } else if (existingSecurityByUserCode.isPresent()) {
	                response.setErrorMessage("UserCode '" + securityDTO.getUserCode() + "' already exists in Security table");
	            } else {
	                response.setErrorMessage("UserCode '" + securityDTO.getUserCode() + "' already exists in Customer table");
	            }
	            return response;
	        } else {
	            // Create new Security and Customer records
	            securityEntity = new Security();
	            securityEntity.setEmail(securityDTO.getEmail());
	            securityEntity.setUserCode(securityDTO.getUserCode());
	            securityEntity.setUserName(securityDTO.getUserName());
	            securityEntity.setIsEmailVerified(securityDTO.getIsEmailVerified() != null ? securityDTO.getIsEmailVerified() : "N");
	            securityEntity.setIsUserCodeVerified(securityDTO.getIsUserCodeVerified() != null ? securityDTO.getIsUserCodeVerified() : "N");
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
	        securityRepo.save(securityEntity);

	        response.setData(securityDTO);
	        return response;
	    }

	public String generateCustomerNumber() {
		PersonSequence seq = personSequenceRepository.save(new PersonSequence());
		Long nextVal = seq.getId();
		return "T" + String.format("%09d", nextVal);
	}
	@Transactional
    public com.SecureAccessPortal.Service.ResponseEntity<SecurityDTO> registerUser(SecurityDTO securityDTO, String userCode) {
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
        Optional<Security> existingSecurityByEmail = securityRepo.findByEmailAndDeletedFlag(securityDTO.getEmail(), "N");
        Optional<Customer> existingCustomerByEmail = customerRepo.findByEmail(securityDTO.getEmail());
        Optional<Security> existingSecurityByUserCode = securityRepo.findByUserCodeAndDeletedFlag(securityDTO.getUserCode(), "N");
        Optional<Customer> existingCustomerByUserCode = customerRepo.findByUserCode(securityDTO.getUserCode());

        if (existingSecurityByEmail.isPresent()) {
            Security security = existingSecurityByEmail.get();
            // Check if userCode matches
            if (!security.getUserCode().equalsIgnoreCase(securityDTO.getUserCode())) {
                response.setErrorMessage("Email '" + securityDTO.getEmail() + "' is associated with a different userCode");
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
            response.setErrorMessage("UserCode '" + securityDTO.getUserCode() + "' already exists in Security table,please check email");
            return response;
        } else if (existingCustomerByUserCode.isPresent()) {
            response.setErrorMessage("UserCode '" + securityDTO.getUserCode() + "' already exists in Customer table,please check email");
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
        securityRepo.save(securityEntity);
        response.setData(securityDTO);
        response.setStatus("Successfully registered, pending for verification");
        return response;
    }

	@Override
	public boolean deleteNoteById(Long id) {
		if (id != null) {
			securityRepo.deleteById(id);
			return true;
		}
		return false;
	}

	@Override
	public ResponseEntity<SecurityDTO> searchUserFromList(SecurityDTO searchRequest, String userCode) {
		ResponseEntity<SecurityDTO> serchResp=  new ResponseEntity<>();
		
		SecurityDTO securityDTO= new SecurityDTO();
		
		if(searchRequest.getUserName() != null) {
			Security user= securityRepo.findByUserName(searchRequest.getUserName(),"N");
			if(user != null) {
				securityDTO.setEmail(user.getEmail());
				securityDTO.setUserCode(user.getUserCode());
				serchResp.setData(securityDTO);
			}else {
			serchResp.setErrorMessage("No user found");
			}	
		}else if(searchRequest.getEmail() != null) {
			Security user= securityRepo.findByEmail(searchRequest.getEmail(),"N");
			if(user != null) {
				securityDTO.setEmail(user.getEmail());
				securityDTO.setUserCode(user.getUserCode());
				serchResp.setData(securityDTO);
			}else {
			serchResp.setErrorMessage("No user found");
			}
		}
		else if(searchRequest.getUserCode() != null) {
			Security user= securityRepo.findByUserCodeDeletedN(searchRequest.getUserCode(),"N");
			if(user != null) {
				securityDTO.setEmail(user.getEmail());
				securityDTO.setUserCode(user.getUserCode());
				serchResp.setData(securityDTO);
			}else {
			serchResp.setErrorMessage("No user found");
			}
		}
		return serchResp;
	}
	
	public String registerWebAuthnCredentials(SecurityDTO request) {
        // Validate userCode
		String message= "";
        if (request.getUserCode() == null) {
            throw new IllegalArgumentException("User ID is mandatory");
        }

        Security security = securityRepo.findByUserCode(request.getUserCode());
        
        security.setCredentialId(request.getCredentialId());
        security.setPublicKey(request.getPublicKey());
        security.setUserHandle(request.getUserHandle());
        security.setSignatureCounter(request.getSignatureCounter());
        security.setUpdateTime(LocalDate.now()); 
        security.setUpdateBy(request.getUserCode()); 
        security.setDeletedFlag("N"); 
        securityRepo.save(security);
        message="Success, fngerprint added successfully";
        return message;
    }
	

}
