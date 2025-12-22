package com.kltn.controller;

import com.kltn.dto.entity.ImageDto;
import com.kltn.model.Image;
import com.kltn.service.impl.ImageServiceImp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.stream.Collectors;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api")
@Api(value = "Tìm nhà trọ", description = "Quản lý hình ảnh")
public class ImageController {
    @Autowired
    private ImageServiceImp imageService;

    // API upload một hình ảnh cho bài viết
    @ApiOperation(value = "Upload 1 hình ảnh cho một tin đăng")
    @PostMapping("/uploadImage/post/{idPost}")
    public ImageDto uploadFile(@PathVariable Long idPost, @RequestParam("file") MultipartFile file) {
        return imageService.uploadFile(idPost, file);
    }

    // API xóa tất cả hình ảnh của bài viết
    @ApiOperation(value = "Delete hình ảnh một tin đăng")
    @DeleteMapping("/deleteImage/post/{idPost}")
    public void deleteFile(@PathVariable Long idPost) {
        imageService.deleteAllImages(idPost);
    }

    // API upload nhiều hình ảnh cho bài viết
    @ApiOperation(value = "Upload nhiều hình ảnh cho một tin đăng")
    @PostMapping("/uploadMultipleFiles/post/{idPost}")

    public List<ImageDto> uploadMultipleFiles(@PathVariable Long idPost, @RequestParam("files") MultipartFile[] files) {
        return Arrays.asList(files)
                .stream()
                .map(file -> uploadFile(idPost, file)) // Gọi uploadFile cho từng ảnh
                .collect(Collectors.toList());
    }

    // API lấy danh sách hình ảnh dạng byte cho chỉnh sửa bài viết
    @ApiOperation(value = "Lấy danh sách hình ảnh của một tin đăng khi chỉnh sửa tin đăng")
    @GetMapping("/imageByte/post/{idPost}")
    public List<ImageDto> getImageDTOByIdPost(@PathVariable Long idPost) {
        return imageService.getImageDTOByIdPost(idPost);
    }

    // API render hình ảnh từ database thành resource
    @ApiOperation(value = "Render 1 ảnh thành link")
    @GetMapping("/image/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) {
        // Load file from database
        Image image = imageService.getImage(fileId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(image.getFileType()))
                .body(new ByteArrayResource(image.getData()));
    }

    // API lấy danh sách link hình ảnh của bài viết
    @ApiOperation(value = "Lấy danh sách hình ảnh của một tin đăng khi xem chi tiết tin đăng")
    @GetMapping("/image/post/{idPost}")
    public List<String> getImageByIdPost(@PathVariable Long idPost) {
        return imageService.getImageByIdPost(idPost);
    }
}
