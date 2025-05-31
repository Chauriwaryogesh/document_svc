package com.SecureAccessPortal.Service; // Use lowercase for package names

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException.BadRequest;

import com.SecureAccessPortal.CommonConstants.CommonConstant;
import com.SecureAccessPortal.Entity.Employees;
import com.SecureAccessPortal.Entity.PersonSequence;
import com.SecureAccessPortal.Entity.Security;
import com.SecureAccessPortal.Exception.BadRequestException;
import com.SecureAccessPortal.Modal.EmpRequestforUpdate;
import com.SecureAccessPortal.Modal.EmployeeDTO;
import com.SecureAccessPortal.Modal.SecurityDTO;
import com.SecureAccessPortal.Modal.WorkItemDTO;
import com.SecureAccessPortal.Repo.IEmployeeRepo;
import com.SecureAccessPortal.Repo.ISecurityRepo;
import com.SecureAccessPortal.Repo.PersonSequenceRepository;
import com.SecureAccessPortal.Transformer.IEmployeeMapper;
import com.SecureAccessPortal.Transformer.SecurityMapper;
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

	@Override
	public List<EmployeeDTO> fetchEmpList(String id, String userId) {
		// Convert the String id to Long (assuming id is a numeric string)
		List<EmployeeDTO> employeeList = new ArrayList<>();
		EmployeeDTO employeeDTO = new EmployeeDTO();

		// Long employeeId = Long.parseLong(id);
		try {
			// for Security
			List<Security> security = securityRepo.findAll();
			boolean Notfound = security.stream().anyMatch(userCode -> userCode.getUserCode().equalsIgnoreCase(userId));

			if (!Notfound) {
				throw new BadRequestException("Invalid User" + userId);
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
	public EmployeeDTO updateEmployee(EmpRequestforUpdate employeeRequest, String userId) {
		EmployeeDTO employeeDTO = new EmployeeDTO();
		try {
			// for Security
			List<Security> security = securityRepo.findAll();
			boolean Notfound = security.stream().anyMatch(userCode -> userCode.getUserCode().equalsIgnoreCase(userId));

			if (!Notfound) {
				throw new BadRequestException("Invalid User" + userId);
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
				workItemRequest.setCreatedBy(userId);
				workItemRequest.setWorkType(CommonConstant.ADD_NEW_EMPLOYEE);
				WorkItemDTO workItem = workItemService.createWorkItem(workItemRequest, userId);

				// Implement Email API. to Share Info.
				if (employeeRequest.getEmail() != null) {
					String response = otpService.sendDetailEmail(employeeRequest.getEmail(), emplo.getId(), workItem,
							userId);
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
			workItemRequest.setCreatedBy(userId);
			workItemRequest.setWorkType(CommonConstant.ADD_NEW_EMPLOYEE);
			WorkItemDTO workItem = workItemService.createWorkItem(workItemRequest, userId);

			// Implement Email API. to Share Info.
			if (employeeRequest.getEmail() != null) {
				String response = otpService.sendDetailEmail(employeeRequest.getEmail(), id, workItem, userId);
			}
		}

		return employeeDTO;
	}

	@Override
	public List<EmployeeDTO> fetchAllEmployee(String userId) {
		try {
			// for Security
			List<Security> security = securityRepo.findAll();
			boolean Notfound = security.stream().anyMatch(userCode -> userCode.getUserCode().equalsIgnoreCase(userId));

			if (!Notfound) {
				throw new BadRequestException("Invalid User" + userId);
			}

		} catch (BadRequest e) {
			e.getMessage();
		}
		List<Employees> employee = employeeRepo.findAll();
		List<EmployeeDTO> employDTO = employeeMapper.mapAllEmployee(employee);
		return employDTO;
	}

	@Override
	public byte[] fetchEmployeeDBforPDF(String id, String userId) throws IOException {
		final String DIRECTORY = "D:/Employee_PDFs/";
		Employees employee = new Employees();
		List<Employees> employeesList = new ArrayList<>();
		// Ensure directory exists
		Files.createDirectories(Paths.get(DIRECTORY));

		try {
			// for Security
			List<Security> security = securityRepo.findAll();
			boolean Notfound = security.stream().anyMatch(userCode -> userCode.getUserCode().equalsIgnoreCase(userId));

			if (!Notfound) {
				throw new BadRequestException("Invalid User" + userId);
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
	public List<SecurityDTO> fetchListOfUsers(String id, String userId){
		List<SecurityDTO> allUsers = List.of();
		List<Security> securityList =new ArrayList<>();
		try { 
			if(id != null) {
				Security security = securityRepo.findById(id, "N");
				securityList.add(security);
			}else {
				 securityList = securityRepo.findAll("N");
			}
			allUsers = securityMapper.mapSecurity(securityList,userId);
		} catch (Exception e) {
			e.getCause();
		}
		return allUsers;
	}

	@Override
	public SecurityDTO updateSecurity(SecurityDTO securityDTO, String userId) {
		Security security = securityRepo.findByEmail(securityDTO.getEmail(), "N");
		if (security != null) {
			if (securityDTO.getEmail().equalsIgnoreCase(security.getEmail())) {
				//security.setId(Long.valueOf( securityDTO.getId()));
				security.setUserCode(securityDTO.getUserCode());
				security.setUserName(securityDTO.getUserName());
				security.setUpdateBy(securityDTO.getUserCode());
				security.setIsEmailVerified(securityDTO.getIsEmailVerified());
				security.setIsUserCodeVerified(securityDTO.getIsUserCodeVerified());
				security.setUpdateTime(String.valueOf(LocalDate.now()));
				if (securityDTO.getCredentialId() != null) {
	                security.setCredentialId(securityDTO.getCredentialId());
	                security.setPublicKey(securityDTO.getPublicKey());
	                security.setUserHandle(securityDTO.getUserHandle());
	                security.setSignatureCounter(securityDTO.getSignatureCounter());
	            }
				if (securityDTO.getRemainingTime() != null) {
					security.setEndTime(securityDTO.getRemainingTime());
				} else {
					LocalDate updateTime = LocalDate.now();
					// Subtract 5 days from updateTime
					LocalDate newDate = updateTime.plusDays(5);
					security.setEndTime(newDate.toString());
					
				}
				securityRepo.save(security);

			}
		} else {
			Security securityEntity = new Security();
			securityEntity.setEmail(securityDTO.getEmail());
			if (securityDTO.getIsEmailVerified() == null) {
				securityEntity.setIsEmailVerified("N");
			} else {
				securityEntity.setIsEmailVerified(securityDTO.getIsEmailVerified());
			}
			if (securityDTO.getIsUserCodeVerified() == null) {
				securityEntity.setIsUserCodeVerified("N");

			} else {
				securityEntity.setIsUserCodeVerified(securityDTO.getIsUserCodeVerified());

			}
			securityEntity.setDeletedFlag("N");
			securityEntity.setUserCode(securityDTO.getUserCode());
			securityEntity.setUserName(securityDTO.getUserName());
			securityEntity.setUpdateBy(securityDTO.getUserCode());
			securityEntity.setUpdateTime(String.valueOf(LocalDate.now()));
			
			// Set WebAuthn credentials if provided
	        if (securityDTO.getCredentialId() != null) {
	            securityEntity.setCredentialId(securityDTO.getCredentialId());
	            securityEntity.setPublicKey(securityDTO.getPublicKey());
	            securityEntity.setUserHandle(securityDTO.getUserHandle());
	            securityEntity.setSignatureCounter(securityDTO.getSignatureCounter());
	        }
	        if (securityDTO.getRemainingTime() != null) {
				securityEntity.setEndTime(securityDTO.getRemainingTime());
			} else {
				LocalDate updateTime = LocalDate.now();
				// Subtract 5 days from updateTime
				LocalDate newDate = updateTime.plusDays(5);
				securityEntity.setEndTime(newDate.toString());

			}

			securityRepo.save(securityEntity);
		}
		return securityDTO;
	}

	public String generateCustomerNumber() {
		PersonSequence seq = personSequenceRepository.save(new PersonSequence());
		Long nextVal = seq.getId();
		return "T" + String.format("%09d", nextVal);
	}

	@Override
	public String registerUser(SecurityDTO securityDTO, String userId) {
		String message = "";
		Security security = securityRepo.findByEmail(securityDTO.getEmail(), "N");
		if ( security != null && security.getEmail().equalsIgnoreCase(securityDTO.getEmail()) && security.getUserCode().equalsIgnoreCase(securityDTO.getUserCode())
				&& security.getIsEmailVerified().equals("Y") && security.getIsUserCodeVerified().equals("Y")) {
			message = "User already Exist in System and Verified Please to go login Page";
		}else if ( security != null && security.getEmail().equalsIgnoreCase(securityDTO.getEmail()) && security.getUserCode().equalsIgnoreCase(securityDTO.getUserCode())
				&& security.getIsEmailVerified().equals("N") && security.getIsUserCodeVerified().equals("N")) {
			message = "User already Exist in System and Pending for Verification";
		}
		else {
			Security securityEntity = new Security();
			securityEntity.setEmail(securityDTO.getEmail());
			if (securityDTO.getIsEmailVerified() == null) {
				securityEntity.setIsEmailVerified("N");
			} else {
				securityEntity.setIsEmailVerified(securityDTO.getIsEmailVerified());
			}
			if (securityDTO.getIsUserCodeVerified() == null) {
				securityEntity.setIsUserCodeVerified("N");

			} else {
				securityEntity.setIsUserCodeVerified(securityDTO.getIsUserCodeVerified());

			}
			securityEntity.setDeletedFlag("N");
			securityEntity.setUserCode(securityDTO.getUserCode());
			securityEntity.setUserName(securityDTO.getUserName());
			securityEntity.setUpdateBy(securityDTO.getUserCode());
			securityEntity.setUpdateTime(String.valueOf(LocalDate.now()));
			if (securityDTO.getRemainingTime() != null) {
				securityEntity.setEndTime(securityDTO.getRemainingTime());
			} else {
				LocalDate updateTime = LocalDate.now();
				// Subtract 5 days from updateTime
				LocalDate newDate = updateTime.plusDays(5);
				securityEntity.setEndTime(newDate.toString());

			}
			securityRepo.save(securityEntity);
			message = "Successfully Register, Pending for Verification";
		}
		return message;
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
	public ResponseEntity<SecurityDTO> searchUserFromList(SecurityDTO searchRequest, String userId) {
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
		return serchResp;
	}
	
	public String registerWebAuthnCredentials(SecurityDTO request) {
        // Validate userId
		String message= "";
        if (request.getUserCode() == null) {
            throw new IllegalArgumentException("User ID is mandatory");
        }

        Security security = securityRepo.findByUserCode(request.getUserCode());
        
        security.setCredentialId(request.getCredentialId());
        security.setPublicKey(request.getPublicKey());
        security.setUserHandle(request.getUserHandle());
        security.setSignatureCounter(request.getSignatureCounter());
        security.setUpdateTime(String.valueOf(System.currentTimeMillis())); 
        security.setUpdateBy(request.getUserCode()); 
        security.setDeletedFlag("N"); 
        securityRepo.save(security);
        message="Success, fngerprint added successfully";
        return message;
    }
	

}
