package com.example.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Photo;

public interface IPhoto extends JpaRepository<Photo, Long>{

}
