package com.example.mapper;

import java.util.List;

import com.example.dto.EmpRequestforUpdate;
import com.example.dto.EmployeeDTO;
import com.example.entity.Employees;

public interface IEmployeeMapper {
	public Employees mapEmployeeRequest(EmpRequestforUpdate employeeRequest);

	public List<EmployeeDTO> mapAllEmployee(List<Employees> employee);

	public EmployeeDTO convertToDTO(Employees empSave);
}
