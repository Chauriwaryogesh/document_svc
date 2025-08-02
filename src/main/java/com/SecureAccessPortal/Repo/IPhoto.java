package com.SecureAccessPortal.Repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.SecureAccessPortal.Entity.Photo;

public interface IPhoto extends JpaRepository<Photo, Long> {

}
