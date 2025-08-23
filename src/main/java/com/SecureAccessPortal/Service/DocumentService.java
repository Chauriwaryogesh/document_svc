package com.SecureAccessPortal.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.SecureAccessPortal.CommonConstants.CommonConstant;
import com.SecureAccessPortal.CommonConstants.ErrorConstants;
import com.SecureAccessPortal.Entity.DocumentEntity;
import com.SecureAccessPortal.Entity.Note;
import com.SecureAccessPortal.Entity.Photo;
import com.SecureAccessPortal.Modal.DocumentDTO;
import com.SecureAccessPortal.Modal.NotesDTO;
import com.SecureAccessPortal.Repo.CustomerRepo;
import com.SecureAccessPortal.Repo.DocumentDtlsRepo;
import com.SecureAccessPortal.Repo.IPhoto;
import com.SecureAccessPortal.Repo.NotesRepo;
import com.SecureAccessPortal.Transformer.DocumentMapper;

import jakarta.persistence.criteria.Predicate;

@Service
public class DocumentService implements IDocumentService {
	
	private static final Logger logger = LoggerFactory.getLogger(DocumentService.class);

	@Autowired
	private DocumentDtlsRepo docRepo;

	@Autowired
	private DocumentMapper documentMapper;

	@Autowired
	private NotesRepo noteRepo;

	@Autowired
	private IPhoto photoRepository;
	
	@Autowired
	private CustomerRepo customerRepository;

	@Override
	public DocumentEntity getDocumentdtls(String id, String docName, String userCode) {
		// List<DocumentDTO> documentDto = new ArrayList<>();
		Optional<DocumentEntity> documents = docRepo.findById(Long.valueOf(id));
		DocumentEntity document = null;
		if (documents.isPresent()) {
			document = documents.get();
			// documentDto = documentMapper.mapDOcumentDtls(documents);
		}
		return document;
	}


	@Override
	public List<DocumentDTO> getAllDocuments(String userCode) {
		List<DocumentEntity> documents = docRepo.findAll();
		return documents.stream().map(file -> {
			DocumentDTO document = new DocumentDTO();
//			document.setId(String.valueOf(file.getId()));
//			document.setDocName(file.getDocName());
//			document.setDocType(file.getDocType());
//			document.setCreatedBy(file.getCreatedBy());
//			document.setUpdatedBy(file.getUpdatedBy());
//			document.setData(file.getData());
			return document;
		}).collect(Collectors.toList());
	}

	@Override
	public String addNotes(NotesDTO note, String userCode) {
		Note notes = new Note();
		String message = "";
		try {
			switch (note.getAction()) {
			case CommonConstant.CREATE -> {
				// generate Id
				String id = generateNotesId();
				notes.setId(id);
				notes.setText(note.getText());
				notes.setAction(note.getAction());
				notes.setTitle(note.getTitle());
				notes.setContent(note.getContent());
				notes.setCategory(note.getCategory());
				notes.setPriority(note.getPriority());
				notes.setRelatedTo(note.getRelatedTo());
				if (note.getDocName() != null) {
					byte[] fileBytes = Base64.getDecoder().decode(note.getFileData());
					notes.setAttachment(fileBytes);
					notes.setDocName(note.getDocName());
					notes.setDocType(note.getDocType());
				}
				notes.setCreatedDate(LocalDateTime.now());
				notes.setCreatedBy(userCode);
				notes.setDeleted(false);
				if (note.getCustomerNo() != null) {
					notes.setCustomerNo(note.getCustomerNo());
				}
				noteRepo.save(notes);
				message = "Notes saved Successfully";
			}
			case CommonConstant.UPDATE -> {
				if (note.getId() != null) {
					Optional<Note> byId = noteRepo.findById(note.getId());
					Note notesDb = byId.get();
					if (note.getCategory() != null) {
						notesDb.setCategory(note.getCategory());
					}
					if (note.getPriority() != null) {
						notesDb.setPriority(note.getPriority());
					}
					if (note.getContent() != null) {
						notesDb.setContent(note.getContent());
					}
					if (note.getTitle() != null) {
						notesDb.setTitle(note.getTitle());
					}
					if (note.getFileData() != null) {
						byte[] fileBytes = Base64.getDecoder().decode(note.getFileData());
						notesDb.setAttachment(fileBytes);
						notesDb.setDocName(note.getDocName());
						notesDb.setDocType(note.getDocType());
					}
					if (note.getRelatedTo() != null) {
						notesDb.setRelatedTo(note.getRelatedTo());
					}
					notesDb.setUpdatedBy(userCode);
					notesDb.setUpdatedDate(LocalDateTime.now());
					noteRepo.save(notesDb);
					message = "Notes Updated Successfully";

				}
			}
			}
		} catch (Exception e) {
			logger.error("issue while saving notes");
			message = CommonConstant.FAILURE;
		}
		return message;
	}

	@Override
	public void savePhoto(String name, MultipartFile file) throws IOException {
		Photo photo = new Photo();
		photo.setName(name);
		photo.setContentType(file.getContentType());
		photo.setData(file.getBytes());

		photoRepository.save(photo);
	}

	@Override
	public List<DocumentDTO> getCaptureAllDocuments(String userCode) {
		List<Photo> documents = photoRepository.findAll();
		return documents.stream().map(file -> {
			DocumentDTO document = new DocumentDTO();
			document.setId(String.valueOf(file.getId()));
			document.setDocName(file.getName());
			document.setDocType(file.getContentType());
			document.setCreatedBy(userCode);
			document.setUpdatedBy(userCode);
			document.setData(file.getData());
			return document;
		}).collect(Collectors.toList());
	}

	@Override
	public DocumentDTO getCaptureDocumentdtls(String id, String docName, String userCode) {
		Optional<Photo> documents = photoRepository.findById(Long.valueOf(id));
		DocumentDTO document = new DocumentDTO();
		if (documents.isPresent()) {
			Photo file = documents.get();
			document.setId(String.valueOf(file.getId()));
			document.setDocName(file.getName());
			document.setDocType(file.getContentType());
			document.setCreatedBy("SYSTEM");
			document.setUpdatedBy("SYSTEM");
			document.setData(file.getData());
		}
		return document;
	}

	@Override
	public Page<NotesDTO> getList(String customerNo, String priority, String userCode, String startDate, String endDate,
		boolean deleted,	String id,int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
		Specification<Note> spec = (root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();

			if (customerNo != null) {
				predicates.add(cb.equal(root.get("customerNo"), customerNo));

			}
			if (id != null) {
				predicates.add(cb.equal(root.get("id"), id));

			}
			if (priority != null) {
				predicates.add(cb.equal(root.get("priority"), priority));

			}
			if (Boolean.TRUE.equals(deleted)) {
				predicates.add(cb.equal(root.get("deleted"), deleted));
			}else {
				predicates.add(cb.equal(root.get("deleted"), deleted));
			}
			
			if (startDate != null && !startDate.isEmpty()) {
				try {
					LocalDateTime startDateTime = LocalDateTime.parse(startDate + " 00:00:00",
							DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
					predicates.add(cb.greaterThanOrEqualTo(root.get("createdTime"), startDateTime));
				} catch (DateTimeParseException e) {
					System.err.println("Invalid startDate format: " + startDate + ". Skipping date filter.");
				}
			}
			if (endDate != null && !endDate.isEmpty()) {
				try {
					LocalDateTime endDateTime = LocalDateTime.parse(endDate + " 23:59:59",
							DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
					predicates.add(cb.lessThanOrEqualTo(root.get("createdTime"), endDateTime));
				} catch (DateTimeParseException e) {
					System.err.println("Invalid endDate format: " + endDate + ". Skipping date filter.");
				}
			}
			return cb.and(predicates.toArray(new Predicate[0]));
		};
		try {
			Page<Note> notesData = noteRepo.findAll(spec, pageable);
			return notesData.map(note ->{
				NotesDTO notesDTO = new NotesDTO();
				notesDTO.setText(note.getText());
				notesDTO.setCreatedDate(note.getCreatedDate());
				notesDTO.setUpdatedDate(note.getUpdatedDate());
				notesDTO.setCreatedBy(note.getCreatedBy());
				notesDTO.setAction(note.getAction());
				notesDTO.setCategory(note.getCategory());
				notesDTO.setContent(note.getContent());
				notesDTO.setCustomerNo(note.getCustomerNo());
				
				if(note.getAttachment() != null ) {
					String fileBytes = Base64.getEncoder().encodeToString(note.getAttachment());
					notesDTO.setViewDocument(fileBytes);
					notesDTO.setDocName(note.getDocName());
					notesDTO.setDocType(note.getDocType());
				}
				
				notesDTO.setId(String.valueOf(note.getId()));
				notesDTO.setPriority(note.getPriority());
				notesDTO.setRelatedTo(note.getRelatedTo());
				notesDTO.setUpdatedBy(note.getUpdatedBy());
				notesDTO.setTitle(note.getTitle());
				
				return notesDTO;
			});
		} catch (Exception e) {
			e.printStackTrace();
			return Page.empty(pageable);
		}
	}

	@Override
	public String updateStatus(NotesDTO note, String userCode) {
		String message = "";

		if (note.getId() != null) {
			Optional<Note> updateStatus = noteRepo.updateStatus(note.getId());
			if (updateStatus.isEmpty()) {
				message = "Invalid id please check Id no "+ note.getId() ;
				return message;
			} else {
				Note note2 = updateStatus.get();
				if (Boolean.TRUE.equals(note.isDeleted())) {
					note2.setDeleted(note.isDeleted());
					message = "Deleted Successfully";
				} else {
					note2.setDeleted(Boolean.FALSE);
					message = "Restore  Successfully";
				}
				note2.setUpdatedBy(userCode);
				note2.setUpdatedDate(LocalDateTime.now());
				Note save = noteRepo.save(note2);
				message = message +" id Notes No "+ save.getId();

			}
		}

		return message;
	}
	
	
	public   String generateNotesId() {
		String prefix = "NOTE/";
		int currentYear = LocalDate.now().getYear();
		String latestComplaintNumber = noteRepo.findLatestByNotesId();
		int nextNumber = 1;
		if (latestComplaintNumber != null && latestComplaintNumber.startsWith(prefix)
				&& latestComplaintNumber.endsWith("/" + currentYear)) {
			String numberPart = latestComplaintNumber.replace(prefix, "").replace("/" + currentYear, "");
			try {
				nextNumber = Integer.parseInt(numberPart) + 1;
			} catch (NumberFormatException e) {
			}
		}
		return String.format("%s%06d/%d", prefix, nextNumber, currentYear);
	}

	@Override
	public Page<DocumentDTO> myDocuments(String documentId, String documentName, String policyNumber, String type,
			String customerNumber, String startDate, String endDate, int page, int size, String bankAccountNumber, String status, boolean deleted) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("createdTime").descending());
		Specification<DocumentEntity> spec = (root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();
			if (documentId != null && !documentId.isEmpty()) {
				predicates.add(cb.equal(root.get("documentId"), documentId));
			}
			if (documentName != null && !documentName.isEmpty()) {
				predicates.add(cb.equal(root.get("documentName"), documentName));
			}
			if (policyNumber != null && !policyNumber.isEmpty()) {
				predicates.add(cb.equal(root.get("policyNumber"), policyNumber));
			}
			if (type != null && !type.isEmpty()) {
				predicates.add(cb.equal(root.get("type"), type));
			}
			if (customerNumber != null && !customerNumber.isEmpty()) {
				predicates.add(cb.equal(root.get("customerNumber"), customerNumber));
			}
			
			if (bankAccountNumber != null && !bankAccountNumber.isEmpty()) {
				predicates.add(cb.equal(root.get("bankAccountNumber"), bankAccountNumber));
			}
			if (status != null && !status.isEmpty()) {
				predicates.add(cb.equal(root.get("status"), status));
			}
			if (Boolean.TRUE.equals(deleted)) {
				predicates.add(cb.equal(root.get("deletedFlag"), CommonConstant.Y));
			}else {
				predicates.add(cb.equal(root.get("deletedFlag"), CommonConstant.N));
			}
			
			if (startDate != null && !startDate.isEmpty()) {
				try {
					LocalDateTime startDateTime = LocalDateTime.parse(startDate + " 00:00:00",
							DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
					predicates.add(cb.greaterThanOrEqualTo(root.get("createdTime"), startDateTime));
				} catch (DateTimeParseException e) {
					System.err.println("Invalid startDate format: " + startDate + ". Skipping date filter.");
				}
			}
			if (endDate != null && !endDate.isEmpty()) {
				try {
					LocalDateTime endDateTime = LocalDateTime.parse(endDate + " 23:59:59",
							DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
					predicates.add(cb.lessThanOrEqualTo(root.get("endTime"), endDateTime));
				} catch (DateTimeParseException e) {
					System.err.println("Invalid endDate format: " + endDate + ". Skipping date filter.");
				}
			}
			return cb.and(predicates.toArray(new Predicate[0]));

		};
		Page<DocumentEntity> documents = docRepo.findAll(spec, pageable);
		try {
			return documents.map(document -> {
				DocumentDTO documentDTO = new DocumentDTO();
				documentDTO.setDocumentId(document.getDocumentId());
				documentDTO.setDocumentName(document.getDocumentName());
				documentDTO.setType(document.getType());
				documentDTO.setPolicyNumber(document.getPolicyNumber());
				documentDTO.setBankAccountNumber(document.getBankAccountNumber());
				documentDTO.setStatus(document.getStatus());
				documentDTO.setCustomerNumber(document.getCustomerNumber());
				documentDTO.setCreatedBy(document.getCreatedBy());
				documentDTO.setUpdatedBy(document.getUpdatedBy());
				documentDTO.setCreatedTime(String.valueOf(document.getCreatedTime()));
				documentDTO.setEndTime(String.valueOf(document.getEndTime()));
				documentDTO.setData(document.getData());
				return documentDTO;
			});
		} catch (Exception e) {
			return Page.empty(pageable);
		}

	}
	
	public String generateDocumentId() {
		String prefix = "DOC/";
		int currentYear = LocalDate.now().getYear();
		String latestComplaintNumber = docRepo.findLatestDocumentId();
		int nextNumber = 1;
		if (latestComplaintNumber != null && latestComplaintNumber.startsWith(prefix)
				&& latestComplaintNumber.endsWith("/" + currentYear)) {
			String numberPart = latestComplaintNumber.replace(prefix, "").replace("/" + currentYear, "");
			try {
				nextNumber = Integer.parseInt(numberPart) + 1;
			} catch (NumberFormatException e) {
				// Fallback to 1 if parsing fails
			}
		}
		return String.format("%s%06d/%d", prefix, nextNumber, currentYear);
	}


	@Override
	public ResponseEntity<List<DocumentDTO>> uploadDocument(List<DocumentDTO> documents, String userCode) {
		List<DocumentEntity> documentList = new ArrayList<DocumentEntity>();
		List<DocumentDTO> listOfDocuments = new ArrayList<DocumentDTO>();
		ResponseEntity<List<DocumentDTO>>  response= new ResponseEntity<>();
		try {
			documentList = documents.stream().map(document -> {
				DocumentEntity documentDTO = new DocumentEntity();
				documentDTO.setDocumentId(generateDocumentId());
				documentDTO.setDocumentName(document.getDocumentName());
				documentDTO.setType(document.getType());
				documentDTO.setPolicyNumber(document.getPolicyNumber());
				documentDTO.setCustomerNumber(document.getCustomerNumber());
				documentDTO.setBankAccountNumber(document.getBankAccountNumber());
				documentDTO.setStatus(CommonConstant.IN_PROGRESS);
				documentDTO.setCreatedBy(document.getCreatedBy());
				documentDTO.setUpdatedBy(document.getUpdatedBy());
				documentDTO.setDeletedFlag(CommonConstant.N);
				documentDTO.setCreatedTime(LocalDateTime.now());
				documentDTO.setEndTime(LocalDateTime.now());
				documentDTO.setData(document.getData());

				DocumentEntity save = docRepo.save(documentDTO);
				return save;
			}).collect(Collectors.toList());
			listOfDocuments = documentMapper.mapAllDocuments(documentList);
			response.setData(listOfDocuments);
		} catch (Exception e) {
			e.printStackTrace();
			response.setErrorMessage(ErrorConstants.FAILURE);
		}
		return  response;
	}


	@Override
	public ResponseEntity<DocumentDTO> updateStatusOfDocument(DocumentDTO documentDTO, String userCode) {
		ResponseEntity<DocumentDTO> response = new ResponseEntity<DocumentDTO>();
		DocumentDTO document = new DocumentDTO();
		try {
			DocumentEntity documentEntity = docRepo.findbyDocumentId(documentDTO.getDocumentId());
			documentEntity.setUpdatedBy(userCode);
			documentEntity.setEndTime(LocalDateTime.now());
			switch (documentDTO.getAction()) {
			case CommonConstant.DELETE -> {
				documentEntity.setDeletedFlag(CommonConstant.Y);
			}
			case CommonConstant.UPDATE_STATUS -> {
				documentEntity.setStatus(documentDTO.getStatus());
			}
			case CommonConstant.RESTORE -> {
				documentEntity.setDeletedFlag(CommonConstant.N);
			}
			case CommonConstant.PERMANENT_DELETE -> {
				docRepo.deleteByDocumentId(documentDTO.getDocumentId());
				break;
			}
			}
			docRepo.save(documentEntity);
			document.setDocumentId(documentDTO.getDocumentId());
			document.setStatus(documentDTO.getStatus());
			response.setData(document);
			response.setStatus(CommonConstant.SUCCESS);
		} catch (Exception e) {
			response.setErrorMessage(ErrorConstants.FAILURE);
			e.printStackTrace();
		}
		return response;
	}
}
