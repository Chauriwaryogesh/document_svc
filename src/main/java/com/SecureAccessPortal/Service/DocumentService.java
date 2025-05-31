package com.SecureAccessPortal.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.SecureAccessPortal.Entity.CapturePhoto;
import com.SecureAccessPortal.Entity.Note;
import com.SecureAccessPortal.Entity.Photo;
import com.SecureAccessPortal.Modal.AddDocument;
import com.SecureAccessPortal.Modal.NotesDTO;
import com.SecureAccessPortal.Modal.PhotoDTO;
import com.SecureAccessPortal.Repo.ICapturePhtoRepo;
import com.SecureAccessPortal.Repo.IPhoto;
import com.SecureAccessPortal.Repo.NotesRepo;
import com.SecureAccessPortal.Transformer.DocumentMapper;

@Service
public class DocumentService implements IDocumentService {

	@Autowired
	private ICapturePhtoRepo docRepo;

	@Autowired
	private DocumentMapper documentMapper;
	
	@Autowired
	private NotesRepo noteRepo;
	
	 @Autowired
	 private IPhoto photoRepository;
	 
	@Override
	public CapturePhoto getDocumentdtls(String id, String docName, String userId) {
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
	public String uploadDocService(List<AddDocument> documentList, String userId) {

		List<CapturePhoto> document = documentMapper.uploadDoc(documentList);

		docRepo.save(document.get(0));

		String str = "Document uplaod successfully";

		return str;
	}

	public CapturePhoto uploadDocument(MultipartFile file,String docName, String userId) throws IOException {
		CapturePhoto document = new CapturePhoto();
		document.setDocId(String.valueOf(UUID.randomUUID()));
		document.setDocName(docName);
		document.setDocType(file.getContentType());
		document.setCreatedBy(userId);
		document.setUpdatedBy(userId);
		document.setData(file.getBytes());

		return docRepo.save(document);
	}

	@Override
	public List<PhotoDTO> getAllDocuments(String userId) {
		List<CapturePhoto> documents = docRepo.findAll();
		return  documents.stream().map(file ->{
			PhotoDTO document= new PhotoDTO();
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
	public String save(NotesDTO note) {
		Note notes= new Note();
		String message="";
		notes.setText(note.getText());
		notes.setCreatedAt(LocalDateTime.now());
		notes.setUpdatedAt(LocalDateTime.now());
		noteRepo.save(notes);
		message="Notes saved Successfully";
		return message;
	}

	@Override
	public List<NotesDTO> getList(String userId) {
		List<Note> notes = noteRepo.findAll();
		return notes.stream().map(note -> {
			NotesDTO notesDTO = new NotesDTO();
			notesDTO.setId(note.getId());
			notesDTO.setText(note.getText());
			notesDTO.setCreatedAt(note.getCreatedAt());
			notesDTO.setUpdatedAt(note.getUpdatedAt());
			return notesDTO;
		}).collect(Collectors.toList());
	}

	@Override
	 public boolean deleteNoteById(Long id) {
        if (noteRepo.existsById(id)) {
            noteRepo.deleteById(id);
            return true;
        }
        return false;
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
	public List<PhotoDTO> getCaptureAllDocuments(String userId) {
		List<Photo> documents = photoRepository.findAll();
		return  documents.stream().map(file ->{
			PhotoDTO document= new PhotoDTO();
			document.setId(String.valueOf(file.getId()));
			document.setDocName(file.getName());
			document.setDocType(file.getContentType());
			document.setCreatedBy("SYSTEM");
			document.setUpdatedBy("SYSTEM");
			document.setData(file.getData());
			return document;	
		}).collect(Collectors.toList());
	}

	@Override
	public PhotoDTO getCaptureDocumentdtls(String id, String docName, String userId) {
		Optional<Photo> documents = photoRepository.findById(Long.valueOf(id));
		PhotoDTO document= new PhotoDTO();
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

}
