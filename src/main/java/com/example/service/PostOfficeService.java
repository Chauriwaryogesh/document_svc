package com.example.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.dto.PostalResponse;

@Service
public class PostOfficeService {

	private static final Logger log = LoggerFactory.getLogger(PostOfficeService.class);

	public PostalResponse getPostOfficeInfo(String pincode, String brnchName, String userId) {
		PostalResponse mainResponse = new PostalResponse();
		if (pincode != null) {
			String url = "https://api.postalpincode.in/pincode/" + pincode;
			log.info(url);
			RestTemplate restTemplate = new RestTemplate();

			try {
				org.springframework.http.ResponseEntity<PostalResponse[]> response = restTemplate.getForEntity(url,
						PostalResponse[].class);
				PostalResponse[] resultArray = response.getBody();

				if (resultArray != null && resultArray.length > 0) {
					mainResponse = resultArray[0];
				}
			} catch (Exception e) {
				e.printStackTrace(); // handle error gracefully
			}

		} else if (brnchName != null) {
			// String encodedBranch = URLEncoder.encode(brnchName,
			// StandardCharsets.UTF_8).replace("+", "%20");
			String url = "https://api.postalpincode.in/postoffice/" + brnchName;
			try {
				RestTemplate restTemplate = new RestTemplate();
				org.springframework.http.ResponseEntity<PostalResponse[]> response = restTemplate.getForEntity(url,
						PostalResponse[].class);

				PostalResponse[] resultArray = response.getBody();
				if (resultArray != null && resultArray.length > 0) {
					mainResponse = resultArray[0];
				}
			} catch (HttpClientErrorException.NotFound e) {
				System.out.println("404 - Branch name not found: " + brnchName);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return mainResponse;
	}

}
