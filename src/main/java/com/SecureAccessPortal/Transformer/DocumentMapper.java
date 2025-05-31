package com.SecureAccessPortal.Transformer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.SecureAccessPortal.Entity.CapturePhoto;
import com.SecureAccessPortal.Modal.AddDocument;
import com.SecureAccessPortal.Modal.PhotoDTO;

@Component
public class DocumentMapper {

	public List<PhotoDTO> mapDOcumentDtls(Optional<CapturePhoto> documentOpt) {
		List<PhotoDTO> documentList = new ArrayList<>();
		CapturePhoto document = documentOpt.get();
		return documentList.stream().map(list -> {
			PhotoDTO documentDTO = new PhotoDTO();
			documentDTO.setId(document.getDocId());
			documentDTO.setDocName(document.getDocName());
			documentDTO.setDocType(document.getDocType());
			documentDTO.setCreatedBy(document.getCreatedBy());
			documentDTO.setUpdatedBy(document.getUpdatedBy());
			return documentDTO;
		}).collect(Collectors.toList());

	}

	public List<CapturePhoto> uploadDoc(List<AddDocument> documentList) {
		// TODO Auto-generated method stub
		return null;
	}

}
