package com.SecureAccessPortal.Service;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.SecureAccessPortal.Modal.EmpRequestforUpdate;
import com.SecureAccessPortal.Modal.EmployeeDTO;
import com.SecureAccessPortal.Modal.SecurityDTO;

public interface ISecrityService{

	public List<EmployeeDTO> fetchEmpList(String id, String userCode);

	public EmployeeDTO updateEmployee(EmpRequestforUpdate employeeRequest, String userCode);

	public List<EmployeeDTO> fetchAllEmployee(String userCode);

	public byte[] fetchEmployeeDBforPDF(String id, String userCode) throws IOException;

	public List<SecurityDTO> fetchListOfUsers(String id, String userCode);
	public SecurityDTO createUser(SecurityDTO securityDTO, String userCode);

	public String registerUser(SecurityDTO securityDTO, String userCode);

	public boolean deleteNoteById(Long id);

	public com.SecureAccessPortal.Service.ResponseEntity<SecurityDTO> searchUserFromList(SecurityDTO searchRequest, String userCode);

	public String registerWebAuthnCredentials(SecurityDTO request);

	

}
