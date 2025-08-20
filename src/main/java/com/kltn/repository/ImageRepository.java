package com.kltn.repository;

import com.kltn.model.Image;
import com.kltn.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, String> {
    List<Image> findImageByPost(Post post);
}
