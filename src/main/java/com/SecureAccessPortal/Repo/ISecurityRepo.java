package com.SecureAccessPortal.Repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.SecureAccessPortal.Entity.Employees;
import com.SecureAccessPortal.Entity.Security;

@Repository
public interface ISecurityRepo  extends JpaRepository<Security, Long>{

	@Query(value="Select * from security s where s.email=?1 and s.deletedFlag=?2",nativeQuery = true)
	  Security findByEmail(String email, String deletedFlag);

	@Query(value="Select * from security s where s.id=?1 and s.deletedFlag=?2",nativeQuery = true)
	Security findById(String id, String string);

	@Query(value="Select * from security s where s.deletedFlag=?1",nativeQuery = true)
	List<Security> findAll(String string);

	@Query("SELECT s FROM Security s WHERE LOWER(s.userName) = LOWER(?1) AND s.deletedFlag = ?2")
	Security findByUserName(String userName, String deletedFlag);

	@Query(value="Select * from security s where s.userCode=?1 AND s.deletedFlag = ?2",nativeQuery = true)
	Optional<Security> findByuserCodeAndDeletedFlag(String userCode,String deletedFlag);

	@Query(value="Select * from security s where s.userCode=?1",nativeQuery = true)
	Security findByUserCode(String userCode);

	@Query(value="Select * from security s where s.userCode=?1 and s.deletedFlag=?2",nativeQuery = true)
	Security findByUserCodeDeletedN(String userCode, String string);


}
