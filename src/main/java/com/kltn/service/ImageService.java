package com.kltn.service;

import com.kltn.dto.entity.ImageDto;
import com.kltn.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {
    ImageDto uploadFile(Long idPost, MultipartFile file);

    Image storeImage(Long idPost, MultipartFile file);

    Image getImage(String imageId);

    List<String> getImageByIdPost(Long idPost);

    void deleteAllImages(Long idPost);

    List<ImageDto> getImageDTOByIdPost(Long idPost);
}
