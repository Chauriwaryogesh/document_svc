package com.example.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mediaController")
public class MediaController {
//	 @Autowired
//	    private MediaService mediaServiec;
	private static final String VIDEO_DIR = "E:/";

	@GetMapping(value = "/fetchVideoFiles")

	public List<String> fetchVideofromDir() throws IOException {

		try (Stream<Path> path = Files.walk(Paths.get(VIDEO_DIR))) {
			return path.filter(Files::isRegularFile).map(Path::toString).filter(file -> file.matches(".*\\.(mp4|.mkv)"))
					.collect(Collectors.toList());
		}

	}
}
