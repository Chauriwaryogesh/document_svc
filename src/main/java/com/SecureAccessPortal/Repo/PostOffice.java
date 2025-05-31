package com.SecureAccessPortal.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.SecureAccessPortal.Entity.PinCodeOrBranch;

public interface PostOffice extends JpaRepository<PinCodeOrBranch, String> {
	
//	@Query(value="Select * from PinCode_Branch t where t.pinCode=?1",nativeQuery = true)
//	List<PinCodeOrBranch> fetchByPincode(String pincode);
//	
//	
//	@Query(value="Select * from PinCode_Branch t where t.branchName=?1",nativeQuery = true)
//	List<PinCodeOrBranch> fetchByBrnchName(String branchName);

}
