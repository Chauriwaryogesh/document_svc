package com.SecureAccessPortal.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.SecureAccessPortal.Entity.FeedbackEntity;

public interface FeedbackRepo extends JpaRepository<FeedbackEntity, Long> {

	@Query("SELECT f FROM FeedbackEntity f WHERE f.userCode= :userCode AND f.deletedFlag= :deletedFlag ORDER BY f.id DESC")
	List<FeedbackEntity> findByUserCodeAndDeletedFlagN(String userCode, String deletedFlag);

}
