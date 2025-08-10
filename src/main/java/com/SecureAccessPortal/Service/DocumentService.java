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
import java.util.UUID;
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
import com.SecureAccessPortal.Entity.CapturePhoto;
import com.SecureAccessPortal.Entity.Note;
import com.SecureAccessPortal.Entity.Photo;
import com.SecureAccessPortal.Modal.AddDocument;
import com.SecureAccessPortal.Modal.NotesDTO;
import com.SecureAccessPortal.Modal.PhotoDTO;
import com.SecureAccessPortal.Repo.CustomerRepo;
import com.SecureAccessPortal.Repo.ICapturePhtoRepo;
import com.SecureAccessPortal.Repo.IPhoto;
import com.SecureAccessPortal.Repo.NotesRepo;
import com.SecureAccessPortal.Transformer.DocumentMapper;

import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;

@Service
public class DocumentService implements IDocumentService {
	
	private static final Logger logger = LoggerFactory.getLogger(DocumentService.class);

	@Autowired
	private ICapturePhtoRepo docRepo;

	@Autowired
	private DocumentMapper documentMapper;

	@Autowired
	private NotesRepo noteRepo;

	@Autowired
	private IPhoto photoRepository;
	
	@Autowired
	private CustomerRepo customerRepository;

	@Override
	public CapturePhoto getDocumentdtls(String id, String docName, String userCode) {
		// List<DocumentDTO> documentDto = new ArrayList<>();
		Optional<CapturePhoto> documents = docRepo.findById(Long.valueOf(id));
		CapturePhoto document = null;
		if (documents.isPresent()) {
			document = documents.get();
			// documentDto = documentMapper.mapDOcumentDtls(documents);
		}
		return document;
	}

	@Override
	public String uploadDocService(List<AddDocument> documentList, String userCode) {

		List<CapturePhoto> document = documentMapper.uploadDoc(documentList);

		docRepo.save(document.get(0));

		String str = "Document uplaod successfully";

		return str;
	}

	public CapturePhoto uploadDocument(MultipartFile file, String docName, String userCode) throws IOException {
		CapturePhoto document = new CapturePhoto();
		document.setDocId(String.valueOf(UUID.randomUUID()));
		document.setDocName(docName);
		document.setDocType(file.getContentType());
		document.setCreatedBy(userCode);
		document.setUpdatedBy(userCode);
		document.setData(file.getBytes());

		return docRepo.save(document);
	}

	@Override
	public List<PhotoDTO> getAllDocuments(String userCode) {
		List<CapturePhoto> documents = docRepo.findAll();
		return documents.stream().map(file -> {
			PhotoDTO document = new PhotoDTO();
			document.setId(String.valueOf(file.getId()));
			document.setDocName(file.getDocName());
			document.setDocType(file.getDocType());
			document.setCreatedBy(file.getCreatedBy());
			document.setUpdatedBy(file.getUpdatedBy());
			document.setData(file.getData());
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
					if (note.getCategory() != null) {
						notesDb.setPriority(message);
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
	public List<PhotoDTO> getCaptureAllDocuments(String userCode) {
		List<Photo> documents = photoRepository.findAll();
		return documents.stream().map(file -> {
			PhotoDTO document = new PhotoDTO();
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
	public PhotoDTO getCaptureDocumentdtls(String id, String docName, String userCode) {
		Optional<Photo> documents = photoRepository.findById(Long.valueOf(id));
		PhotoDTO document = new PhotoDTO();
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

}
