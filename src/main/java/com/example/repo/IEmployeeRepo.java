package com.example.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.Employees;

@Repository
public interface IEmployeeRepo extends JpaRepository<Employees, Long> {

	@Modifying
	@Transactional
	@Query(value = "INSERT INTO employees (first_name, last_name, email) "
			+ "VALUES (:#{#employee.firstName}, :#{#employee.lastName}, :#{#employee.email})", nativeQuery = true)
	public long addNewEmployee(@Param("employee") Employees employee);

}
