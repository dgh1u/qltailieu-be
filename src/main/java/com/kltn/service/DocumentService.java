// src/main/java/com/nckh/motelroom/service/DocumentService.java
package com.kltn.service;

import com.kltn.dto.entity.DocumentDto;
import com.kltn.model.Document;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DocumentService {
    DocumentDto uploadDocument(Long idPost, MultipartFile file);
    Document getDocument(String documentId);
    List<String> getDocumentsByIdPost(Long idPost);
    void deleteSingleDocument(String documentId);
    List<DocumentDto> getDocumentDTOsByIdPost(Long idPost);
    Document getDocumentForDownload(String documentId);
}