// src/main/java/com/nckh/motelroom/repository/DocumentRepository.java
package com.kltn.repository;

import com.kltn.model.Document;
import com.kltn.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, String> {
    List<Document> findDocumentByPost(Post post);
}