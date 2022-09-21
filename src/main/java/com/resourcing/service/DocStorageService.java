
/**
  *	
  *@author:Praveen Gudimalla
  *
  *
  **/

package com.resourcing.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.resourcing.beans.Doc;
import com.resourcing.repository.DocRepository;

@Service
public class DocStorageService {
	@Autowired
	private DocRepository docRepository;

	public Doc saveFile(MultipartFile file, Doc doc) {
		try {
			doc.setDocName(file.getOriginalFilename());
			doc.setData(file.getBytes());
			doc.setDocType(file.getContentType());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return docRepository.save(doc);
	}

	public Doc getFileById(Integer fileId) {
		Optional<Doc> optional = docRepository.findById(fileId);
		Doc doc = null;
		if (optional.isPresent()) {
			doc = optional.get();
		} else {
			throw new RuntimeException("repository:::: Document not found for id:::" + fileId);
		}
		return doc;
	}

	public List<Doc> getFiles() {
		return docRepository.findAll();
	}

	public List<Doc> getAllFilesById(int id) {
		return docRepository.findAllById(id);
	}

	public void deleteFileById(Integer id) {
		try {
			this.docRepository.deleteById(id);
		} catch (DataAccessException ex) {
			throw new RuntimeException(ex.getMessage());
		}
	}
}
