package com.example.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Document;

@Repository
public interface IDocumentRepo extends JpaRepository<Document,Long>{

	//List<Document> uploadDocSrvice(List<Document> document);

}
