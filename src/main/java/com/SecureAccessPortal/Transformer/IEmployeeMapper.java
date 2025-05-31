package com.SecureAccessPortal.Transformer;

import java.util.List;

import com.SecureAccessPortal.Entity.Employees;
import com.SecureAccessPortal.Modal.EmpRequestforUpdate;
import com.SecureAccessPortal.Modal.EmployeeDTO;

public interface IEmployeeMapper {
	public Employees mapEmployeeRequest(EmpRequestforUpdate employeeRequest, String id);

	public List<EmployeeDTO> mapAllEmployee(List<Employees> employee);

	public EmployeeDTO convertToDTO(Employees empSave);
}
