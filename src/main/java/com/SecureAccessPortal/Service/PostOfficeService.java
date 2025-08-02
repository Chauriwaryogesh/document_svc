package com.SecureAccessPortal.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.SecureAccessPortal.Entity.PinCodeOrBranch;
import com.SecureAccessPortal.Modal.PinCodeCount;
import com.SecureAccessPortal.Modal.PinCodeOrBranchDTO;
import com.SecureAccessPortal.Modal.PostalResponse;
import com.SecureAccessPortal.Repo.PostOffice;

@Service
public class PostOfficeService {

	@Autowired
	private PostOffice postOffice;

	private static final Logger log = LoggerFactory.getLogger(PostOfficeService.class);

	public PostalResponse getPostOfficeInfo(String pincode, String brnchName, String userCode) {
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

	public List<PinCodeCount> getPinCodeInfo(String pinCode, String brnchName, String userCode) {
		List<PinCodeOrBranch> pinArray = new ArrayList<>();
		pinArray = postOffice.findAll();

		List<PinCodeCount> pinCodeDTO = new ArrayList<>();
		PinCodeCount pinCodeCount = new PinCodeCount();

		long branch = pinArray.stream().filter(code -> code.getBranchName() != null && !code.getBranchName().isEmpty())
				.map(code -> code.getBranchName()).count();
		long pinc = pinArray.stream().filter(code -> code.getPinCode() != null && !code.getPinCode().isEmpty())
				.map(code -> code.getPinCode()).count();
		pinCodeCount.setPinCount(String.valueOf(pinc));
		pinCodeCount.setBranchCount(String.valueOf(branch));

		if (pinCode != null) {
			pinCodeCount.setPinCodeOrBranchDTO(pinArray.stream().map(pincode -> {
				PinCodeOrBranchDTO dto = new PinCodeOrBranchDTO();
				dto.setId(String.valueOf(pincode.getId()));
				dto.setPincCode(pincode.getPinCode());
				return dto;
			}).collect(Collectors.toList()));
		} else if (brnchName != null) {
			pinCodeCount.setPinCodeOrBranchDTO(pinArray.stream().map(pincode -> {
				PinCodeOrBranchDTO dto = new PinCodeOrBranchDTO();
				dto.setId(String.valueOf(pincode.getId()));
				dto.setBranchName(pincode.getBranchName());
				return dto;
			}).collect(Collectors.toList()));
		} else {
			pinCodeCount.setPinCodeOrBranchDTO(pinArray.stream().map(pincode -> {
				PinCodeOrBranchDTO dto = new PinCodeOrBranchDTO();
				dto.setId(String.valueOf(pincode.getId()));
				dto.setBranchName(pincode.getBranchName());
				dto.setPincCode(pincode.getPinCode());
				return dto;
			}).collect(Collectors.toList()));

		}
		pinCodeDTO.add(pinCodeCount);

		return pinCodeDTO;
	}

}
