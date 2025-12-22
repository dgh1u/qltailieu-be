package com.kltn.service.impl;

import com.kltn.dto.entity.ImageDto;
import com.kltn.exception.DataNotFoundException;
import com.kltn.exception.MyCustomException;
import com.kltn.mapper.ImageMapper;
import com.kltn.model.Image;
import com.kltn.model.Post;
import com.kltn.repository.ImageRepository;
import com.kltn.repository.PostRepository;
import com.kltn.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageServiceImp implements ImageService {
    private final ImageMapper imageMapper;

    private final ImageRepository imageRepository;

    private final PostRepository postRepository;

    // Upload file ảnh cho bài viết
    @Override
    public ImageDto uploadFile(Long idPost, MultipartFile file) {
        // Kiểm tra bài đăng có tồn tại không
        Optional<Post> post = postRepository.findById(idPost);
        if (post.isPresent()) {
            // Lưu ảnh vào database
            Image image = storeImage(idPost, file);
            // Tạo link để truy cập ảnh
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/image/")
                    .path(image.getId())
                    .toUriString();
            // Trả về thông tin ảnh
            return new ImageDto(image.getId(), image.getFileName(), file.getContentType(), fileDownloadUri, idPost);
        } else {
            throw new DataNotFoundException("I can't not found postID " + idPost);
        }
    }

    // Lưu trữ ảnh vào database
    @Override
    public Image storeImage(Long idPost, MultipartFile file) {
        // Lấy tên file và validate
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (fileName.contains("..")) {
                throw new DataNotFoundException("I can't found file name in " + fileName);
            }
            // Tìm bài đăng
            Optional<Post> post = postRepository.findById(idPost);

            // Tạo đối tượng Image và lưu
            Image image = new Image(fileName, file.getContentType(), file.getBytes(), post.get());
            return imageRepository.save(image);
        } catch (Exception e) {
            throw new MyCustomException("Error while i handle save image " + fileName + "!" + e);
        }
    }

    // Lấy ảnh theo ID
    @Override
    public Image getImage(String imageId) {
        return imageRepository.findById(imageId)
                .orElseThrow(() -> new DataNotFoundException("Không tim thấy ảnh có id " + imageId));
    }

    // Lấy danh sách link ảnh theo ID bài viết
    @Override
    public List<String> getImageByIdPost(Long idPost) {
        // Tạo danh sách để chứa link ảnh
        List<String> uri = new ArrayList<>();
        // Tìm bài đăng
        Optional<Post> post = postRepository.findById(idPost);
        // Tìm tất cả ảnh của bài đăng này
        List<Image> images = imageRepository.findImageByPost(post.get());
        for (Image image : images) {
            uri.add(ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/image/")
                    .path(image.getId())
                    .toUriString());
        }
        return uri;
    }

    // Xóa tất cả ảnh của bài viết
    @Override
    public void deleteAllImages(Long idPost) {
        Optional<Post> post = postRepository.findById(idPost);
        if (post.isPresent()) {
            List<Image> images = imageRepository.findImageByPost(post.get());
            imageRepository.deleteAll(images);
        } else {
            throw new DataNotFoundException("I can't not found postID " + idPost);
        }
    }

    // Lấy danh sách ảnh dạng DTO (base64) theo ID bài viết
    @Override
    public List<ImageDto> getImageDTOByIdPost(Long idPost) {
        Optional<Post> post = postRepository.findById(idPost);
        if (post.isPresent()) {
            List<Image> images = imageRepository.findImageByPost(post.get());
            List<ImageDto> imageDtos = new ArrayList<>();
            for (Image image : images) {
                ImageDto imageDto = imageMapper.toDto(image);
                imageDto.setUri(Base64.getEncoder().encodeToString(image.getData()));
                imageDtos.add(imageDto);
            }
            return imageDtos;
        } else {
            throw new DataNotFoundException("I can't not found postID " + idPost);
        }
    }
}
