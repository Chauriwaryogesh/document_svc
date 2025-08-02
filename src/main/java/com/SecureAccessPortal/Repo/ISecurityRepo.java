package com.SecureAccessPortal.Repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.SecureAccessPortal.Entity.Employees;
import com.SecureAccessPortal.Entity.Security;

@Repository
public interface ISecurityRepo extends JpaRepository<Security, Long> {

	@Query("SELECT s FROM Security s WHERE s.email = :email AND s.deletedFlag = :deletedFlag")
	Security findByEmail(String email, String deletedFlag);

	@Query(value = "Select * from security s where s.id=?1 and s.deletedFlag=?2", nativeQuery = true)
	Optional<Security> findByIdAndDeletedFlag(String id, String deletedflag);

	@Query(value = "Select * from security s where s.deletedFlag=?1", nativeQuery = true)
	List<Security> findAll(String string);

	@Query("SELECT s FROM Security s WHERE LOWER(s.userName) = LOWER(?1) AND s.deletedFlag = ?2")
	Security findByUserName(String userName, String deletedFlag);

	@Query(value = "Select * from security s where s.userCode=?1 AND s.deletedFlag = ?2", nativeQuery = true)
	Optional<Security> findByUserCodeAndDeletedFlag(String userCode, String deletedFlag);

	@Query(value = "Select * from security s where s.userCode=?1", nativeQuery = true)
	Security findByUserCode(String userCode);

	@Query(value = "Select * from security s where s.userCode=?1 and s.deletedFlag=?2", nativeQuery = true)
	Security findByUserCodeDeletedN(String userCode, String string);

	@Query("SELECT s FROM Security s WHERE s.email = :email AND s.deletedFlag = :deletedFlag")
	Optional<Security> findByEmailAndDeletedFlag(String email, String deletedFlag);

//    @Query("SELECT s FROM Security s WHERE s.userCode = :userCode AND s.deletedFlag = :deletedFlag")
//    Optional<Security> findByUserCodeAndDeletedFlag(String userCode, String deletedFlag);

	@Query("SELECT s FROM Security s WHERE s.customerNo = :customerNo AND s.deletedFlag = :deletedFlag")
	Optional<Security> findByCustomerNoAndDeletedFlag(String customerNo, String deletedFlag);

	@Query("SELECT s FROM Security s WHERE s.email = :email AND s.userCode = :userCode AND s.deletedFlag = :deletedFlag")
	boolean existsByEmailOrUserCode(String email, String userCode, String deletedFlag);

	@Query("SELECT s FROM Security s WHERE (s.email = :email OR s.userCode = :userCode) AND s.deletedFlag = :deletedflag")
	Optional<Security> findByEmailOrUserCodeAndDeletedFlag(String email, String userCode, String deletedflag);

	Page<Security> findAllByDeletedFlag(String deletedflag, Pageable pageable);

	Page<Security> findAll(Specification<Security> spec, Pageable pageable);

}
