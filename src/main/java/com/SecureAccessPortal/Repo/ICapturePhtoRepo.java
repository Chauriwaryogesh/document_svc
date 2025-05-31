package com.SecureAccessPortal.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.SecureAccessPortal.Entity.CapturePhoto;

@Repository
public interface ICapturePhtoRepo extends JpaRepository<CapturePhoto,Long>{

	//List<Document> uploadDocSrvice(List<Document> document);

}
