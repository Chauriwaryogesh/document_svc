package com.SecureAccessPortal.Service;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.SecureAccessPortal.Modal.EmpRequestforUpdate;
import com.SecureAccessPortal.Modal.EmployeeDTO;
import com.SecureAccessPortal.Modal.SecurityDTO;
import com.SecureAccessPortal.Modal.SetPasswordRequest;

public interface ISecrityService{

	public List<EmployeeDTO> fetchEmpList(String id, String userCode);

	public EmployeeDTO updateEmployee(EmpRequestforUpdate employeeRequest, String userCode);

	public List<EmployeeDTO> fetchAllEmployee(String userCode);

	public byte[] fetchEmployeeDBforPDF(String id, String userCode) throws IOException;

	public List<SecurityDTO> fetchListOfUsers(String id, String userCode);
	public com.SecureAccessPortal.Service.ResponseEntity<SecurityDTO> createUser(SecurityDTO securityDTO, String userCode);

	public com.SecureAccessPortal.Service.ResponseEntity<SecurityDTO> registerUser(SecurityDTO securityDTO, String userCode);

	public boolean deleteNoteById(Long id, String userCode);

	public com.SecureAccessPortal.Service.ResponseEntity<SecurityDTO> searchUserFromList(SecurityDTO searchRequest, String userCode);

	public com.SecureAccessPortal.Service.ResponseEntity<String> registerWebAuthnCredentials(SecurityDTO request);

	public com.SecureAccessPortal.Service.ResponseEntity<String> setPassword(SetPasswordRequest request,
			String userCode);

	public com.SecureAccessPortal.Service.ResponseEntity<String> loginUsingPassword(String email, String userCode,
			String password);

	void logLoginAttempt(String email, String userCode, boolean success, String reason, String loginMethod);

	

}
