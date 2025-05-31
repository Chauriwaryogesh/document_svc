package com.SecureAccessPortal.Controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SecureAccessPortal.JwtUtil.JwtUtil;
import com.SecureAccessPortal.Modal.EmpRequestforUpdate;
import com.SecureAccessPortal.Modal.EmployeeDTO;
import com.SecureAccessPortal.Service.ISecrityService;
import com.SecureAccessPortal.Service.ResponseEntity;

@RestController
@RequestMapping("/Employee")
public class EmployeeController {

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private ISecrityService empService;

	@GetMapping(value = "Jwt Generator")
	public Map<String, String> jwtGenerate(String userId) {
		String token = jwtUtil.generateToken(userId);
		return Map.of("token", token);
	}

	@RequestMapping(value = "getEmployee_Information", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<EmployeeDTO>> getEmployeeList(@RequestParam(value = "Id") String id,
			@RequestHeader(value = "UserId") String userId,
			@RequestHeader(value = "AUthorizationHeader", required = false) String authorizationHeader) {

		ResponseEntity<List<EmployeeDTO>> serviceResponce = new ResponseEntity<>();

		if (!"SYSTEM".equalsIgnoreCase(userId)) {

			serviceResponce.setErrorMessage("Unauthorized user");
		}
		String token = authorizationHeader;
//		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//			token = authorizationHeader.substring(7);
//		} else {
//			serviceResponce.setErrorMessage("Unauthorized user");
//		}
		try {
			String usernameFromToken = jwtUtil.extractUsername(token);
			if (!jwtUtil.validateToken(token, usernameFromToken)) {
				serviceResponce.setErrorMessage("Unauthorized user");
			}
		} catch (Exception ex) {
			serviceResponce.setErrorMessage("Unauthorized user");
		}
		List<EmployeeDTO> empList = empService.fetchEmpList(id,userId);
		serviceResponce.setData(empList);
		return serviceResponce;
	}

	@PostMapping(value = "/create-Update_Employee", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<EmployeeDTO> updateEmployee(@RequestBody EmpRequestforUpdate employeeRequest,
			@RequestHeader(value = "userId", required = false) String userId,
			@RequestHeader(value = "AUthorizationHeader", required = false) String authorizationHeader) {

		ResponseEntity<EmployeeDTO> serviceResponce = new ResponseEntity<>();
		EmployeeDTO employeeDTO = new EmployeeDTO();
		if (employeeRequest == null) {
			serviceResponce.setErrorMessage("Request is null");
		}
		employeeDTO = empService.updateEmployee(employeeRequest,userId);
		serviceResponce.setData(employeeDTO);
		return serviceResponce;
	}

	//@Cacheable("employees")
	@GetMapping(value = "/findAllEmployee", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<EmployeeDTO>> getAllEmployee(String userId) {
		ResponseEntity<List<EmployeeDTO>> serviceResp = new ResponseEntity<List<EmployeeDTO>>();
		List<EmployeeDTO> employeeDTO = empService.fetchAllEmployee(userId);
		serviceResp.setData(employeeDTO);
		return serviceResp;
	}

	 @GetMapping(value = "/generatePDF", produces = MediaType.APPLICATION_PDF_VALUE)
	    public  org.springframework.http.  ResponseEntity<byte[]> generatePdf(
	    		@RequestParam (value="id", required=false)String id,
	    		@RequestHeader (value="userId", required = true) String userId) throws IOException {
	        System.out.println("Generating PDF for Employee ID: " + id);

	        byte[] pdfBytes = empService.fetchEmployeeDBforPDF(id,userId);

	        if (pdfBytes == null || pdfBytes.length == 0) {
	            return org.springframework.http. ResponseEntity.internalServerError()
	                    .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE)
	                    .body("Error generating PDF".getBytes());
	        }

	        return org.springframework.http.ResponseEntity.ok()
	                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=employee_" + id + ".pdf")
	                .contentType(MediaType.APPLICATION_PDF)
	                .body(pdfBytes);
	    }
	    
	    	    
	    
}
