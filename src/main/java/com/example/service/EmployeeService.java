package com.example.service; // Use lowercase for package names

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

import com.example.CommonConstants.CommonConstant;
import com.example.dto.EmpRequestforUpdate;
import com.example.dto.EmployeeDTO;
import com.example.dto.SecurityDTO;
import com.example.dto.WorkItemDTO;
import com.example.entity.Employees;
import com.example.entity.Security;
import com.example.exception.BadRequestException;
import com.example.mapper.IEmployeeMapper;
import com.example.mapper.SecurityMapper;
import com.example.repo.IEmployeeRepo;
import com.example.repo.ISecurityRepo;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

@Service
public class EmployeeService implements IEmployeeService {


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

	@Override
	public List<EmployeeDTO> fetchEmpList(String id, String userId) {
		// Convert the String id to Long (assuming id is a numeric string)
		List<EmployeeDTO> employeeList = new ArrayList<>();
		EmployeeDTO employeeDTO = new EmployeeDTO();
		
		Long employeeId = Long.parseLong(id);
		try {
		//for Security
		List<Security> security= securityRepo.findAll();
		boolean Notfound=security.stream().anyMatch(userCode ->
		userCode.getUserCode().equalsIgnoreCase(userId));
		
		if(!Notfound) {
			throw new BadRequestException("Invalid User"+ userId);
		}

		}catch(BadRequest e) {
			e.getMessage();
		}
		try {
			Optional<Employees> optionalEmployee = employeeRepo.findById(employeeId);
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
			//for Security
			List<Security> security= securityRepo.findAll();
			boolean Notfound=security.stream().anyMatch(userCode ->
			userCode.getUserCode().equalsIgnoreCase(userId));
			
			if(!Notfound) {
				throw new BadRequestException("Invalid User"+ userId);
			}

			}catch(BadRequest e) {
				e.getMessage();
			}
		if (employeeRequest.getId() != null) {
			Optional<Employees> isExisting = employeeRepo.findById(Long.valueOf(employeeRequest.getId()));
			// toUpdateExisting
			if (isExisting.isPresent()) {
				Employees employee = employeeMapper.mapEmployeeRequest(employeeRequest);
				Employees empSave = employeeRepo.save(employee);
				employeeDTO = employeeMapper.convertToDTO(empSave);
				// Create WorkItem whenever added new Employee or update.
				WorkItemDTO workItemRequest= new WorkItemDTO();
				workItemRequest.setComment("WorkItem getting created for Update Employee Details");
				workItemRequest.setCreatedBy(userId);
				workItemRequest.setWorkType(CommonConstant.ADD_NEW_EMPLOYEE);
				WorkItemDTO  workItem =workItemService.createWorkItem( workItemRequest, userId);
				
			}
		} else {
			Employees employee = employeeMapper.mapEmployeeRequest(employeeRequest);
			Employees empSave = employeeRepo.save(employee);
			employeeDTO = employeeMapper.convertToDTO(empSave);
			// Create WorkItem whenever added new Employee or update.
			WorkItemDTO workItemRequest= new WorkItemDTO();
			workItemRequest.setComment("WorkItem getting created for new employee");
			workItemRequest.setCreatedBy(userId);
			workItemRequest.setWorkType(CommonConstant.ADD_NEW_EMPLOYEE);
			WorkItemDTO  workItem =workItemService.createWorkItem( workItemRequest, userId);
			
		}

		return employeeDTO;
	}

	@Override
	public List<EmployeeDTO> fetchAllEmployee(String userId) {
		try {
			//for Security
			List<Security> security= securityRepo.findAll();
			boolean Notfound=security.stream().anyMatch(userCode ->
			userCode.getUserCode().equalsIgnoreCase(userId));
			
			if(!Notfound) {
				throw new BadRequestException("Invalid User"+ userId);
			}
			
			}catch(BadRequest e) {
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
			//for Security
			List<Security> security= securityRepo.findAll();
			boolean Notfound=security.stream().anyMatch(userCode ->
			userCode.getUserCode().equalsIgnoreCase(userId));
			
			if(!Notfound) {
				throw new BadRequestException("Invalid User"+ userId);
			}

			}catch(BadRequest e) {
				e.getMessage();
			}

		// Fetch employee details from DB
		if (id == null) {
			employeesList = employeeRepo.findAll();
			id = "100";

		}

		else {
			Optional<Employees> empFound = employeeRepo.findById(Long.valueOf(id));
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
	public List<SecurityDTO> fetchAllSecurity(String userId) {
		List<SecurityDTO> allSecurity=List.of();
		try {
		List<Security> security= securityRepo.findAll();
		
		allSecurity= securityMapper.mapSecurity(security);
		}catch(Exception e) {
			e.getCause();
		}
		return allSecurity;
	}

	@Override
	public SecurityDTO updateSecurity(SecurityDTO securityDTO, String userId) {
		
		Security securityEntity= new Security();
		securityEntity.setUserCode(securityDTO.getUserCode());
		securityEntity.setUserName(securityDTO.getUserName());
		securityEntity.setUpdateBy(securityDTO.getUserCode());
		securityEntity.setUpdateTime( String.valueOf(LocalDate.now()));
		
//		LocalDate updateTime = LocalDate.now(); // updateTime as a LocalDate
//		LocalDate currentDate = LocalDate.now(); // current date, or another LocalDate value
//		long daysBetween = ChronoUnit.DAYS.between(updateTime, currentDate);
//		securityEntity.setEndTime(String.valueOf(daysBetween)); // days difference as a String
		LocalDate updateTime = LocalDate.now();
		// Subtract 5 days from updateTime
		LocalDate newDate = updateTime.plusDays(5);
		securityEntity.setEndTime(newDate.toString());
		
		securityRepo.save(securityEntity);
		
		return securityDTO;
		
		
	}

}
