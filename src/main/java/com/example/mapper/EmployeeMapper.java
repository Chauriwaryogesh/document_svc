package com.example.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.dto.EmpRequestforUpdate;
import com.example.dto.EmployeeDTO;
import com.example.entity.Employees;

@Component
public class EmployeeMapper implements IEmployeeMapper {

	public Employees mapEmployeeRequest(EmpRequestforUpdate employeeRequest, String id) {

		Employees emp = new Employees();
		if (employeeRequest.getId() != null) {
			emp.setId(employeeRequest.getId());
		}else {
			emp.setId(id);
		}
		emp.setFirstName(employeeRequest.getFirstName());
		emp.setLastName(employeeRequest.getLastName());
		emp.setEmail(employeeRequest.getEmail());
		emp.setAddress(employeeRequest.getAddress());
		emp.setCity(employeeRequest.getCity());
		emp.setCountry(employeeRequest.getCountry());
		emp.setDepartment(employeeRequest.getDepartment());
		emp.setPhone(employeeRequest.getPhone());
		emp.setState(employeeRequest.getState());
		emp.setZipCode(employeeRequest.getZipCode());
		return emp;
	}

	@Override
	public List<EmployeeDTO> mapAllEmployee(List<Employees> employeeRequestList) {

		List<EmployeeDTO> empList = List.of();

		long count = employeeRequestList.stream().count();

		empList = employeeRequestList.stream().map(employeeRequest -> {
			EmployeeDTO emp = new EmployeeDTO();
			if (employeeRequest.getId() != null) {
				emp.setId(String.valueOf(employeeRequest.getId()));

			}
			emp.setCount(count);
			emp.setFirstName(employeeRequest.getFirstName());
			emp.setLastName(employeeRequest.getLastName());
			emp.setEmail(employeeRequest.getEmail());
			emp.setAddress(employeeRequest.getAddress());
			emp.setCity(employeeRequest.getCity());
			emp.setCountry(employeeRequest.getCountry());
			emp.setDepartment(employeeRequest.getDepartment());
			emp.setPhone(employeeRequest.getPhone());
			emp.setState(employeeRequest.getState());
			emp.setZipCode(employeeRequest.getZipCode());
			return emp;
		}).collect(Collectors.toList());

		return empList;
	}

	@Override
	public EmployeeDTO convertToDTO(Employees employeeRequest) {
		EmployeeDTO emp = new EmployeeDTO();
		if (employeeRequest.getId() != null) {
			emp.setId(String.valueOf(employeeRequest.getId()));

		}
		emp.setFirstName(employeeRequest.getFirstName());
		emp.setLastName(employeeRequest.getLastName());
		emp.setEmail(employeeRequest.getEmail());
		emp.setAddress(employeeRequest.getAddress());
		emp.setCity(employeeRequest.getCity());
		emp.setCountry(employeeRequest.getCountry());
		emp.setDepartment(employeeRequest.getDepartment());
		emp.setPhone(employeeRequest.getPhone());
		emp.setState(employeeRequest.getState());
		emp.setZipCode(employeeRequest.getZipCode());
		return emp;

	}
}
