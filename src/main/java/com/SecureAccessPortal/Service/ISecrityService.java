package com.SecureAccessPortal.Service;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.SecureAccessPortal.Modal.EmpRequestforUpdate;
import com.SecureAccessPortal.Modal.EmployeeDTO;
import com.SecureAccessPortal.Modal.SecurityDTO;

public interface ISecrityService{

	public List<EmployeeDTO> fetchEmpList(String id, String userId);

	public EmployeeDTO updateEmployee(EmpRequestforUpdate employeeRequest, String userId);

	public List<EmployeeDTO> fetchAllEmployee(String userId);

	public byte[] fetchEmployeeDBforPDF(String id, String userId) throws IOException;

	public List<SecurityDTO> fetchListOfUsers(String id, String userId);
	public SecurityDTO createUser(SecurityDTO securityDTO, String userId);

	public String registerUser(SecurityDTO securityDTO, String userId);

	public boolean deleteNoteById(Long id);

	public com.SecureAccessPortal.Service.ResponseEntity<SecurityDTO> searchUserFromList(SecurityDTO searchRequest, String userId);

	public String registerWebAuthnCredentials(SecurityDTO request);

	

}
