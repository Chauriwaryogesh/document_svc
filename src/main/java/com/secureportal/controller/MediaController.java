package com.example.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.service.MediaService;

@RestController
@RequestMapping("/mediaController")
public class MediaController {
	 @Autowired
	    private MediaService mediaServiec;
	private static final String VIDEO_DIR = "E:/";

	@GetMapping(value = "/fetchVideoFiles")

	public List<String> fetchVideofromDir() throws IOException {

		try (Stream<Path> path = Files.walk(Paths.get(VIDEO_DIR))) {
			return path.filter(Files::isRegularFile).map(Path::toString).filter(file -> file.matches(".*\\.(mp4|.mkv)"))
					.collect(Collectors.toList());
		}

	}
	   @PostMapping("/upload")
	    public ResponseEntity<String> uploadPhoto(@RequestParam("image") MultipartFile image,
	                                              @RequestParam("name") String name) {
	        try {
	        	mediaServiec.savePhoto(name, image);
	            return ResponseEntity.ok("Photo saved successfully.");
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                                 .body("Error saving photo: " + e.getMessage());
	        }
	    }
}
