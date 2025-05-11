package com.example.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.entity.Photo;
import com.example.repo.IPhoto;

@Service
public class MediaService {

	 @Autowired
	    private IPhoto photoRepository;

	    public void savePhoto(String name, MultipartFile file) throws IOException {
	        Photo photo = new Photo();
	        photo.setName(name);
	        photo.setContentType(file.getContentType());
	        photo.setData(file.getBytes());

	        photoRepository.save(photo);
	    }
}
