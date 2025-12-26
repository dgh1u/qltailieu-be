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

    // API upload một hình ảnh cho tài liệu
    @ApiOperation(value = "Upload 1 hình ảnh cho một tài liệu")
    @PostMapping("/uploadImage/post/{idPost}")
    public ImageDto uploadFile(@PathVariable Long idPost, @RequestParam("file") MultipartFile file) {
        return imageService.uploadFile(idPost, file);
    }

    // API xóa tất cả hình ảnh của tài liệu
    @ApiOperation(value = "Delete hình ảnh một tài liệu")
    @DeleteMapping("/deleteImage/post/{idPost}")
    public void deleteFile(@PathVariable Long idPost) {
        imageService.deleteAllImages(idPost);
    }

    // API upload nhiều hình ảnh cho tài liệu
    @ApiOperation(value = "Upload nhiều hình ảnh cho một tài liệu")
    @PostMapping("/uploadMultipleFiles/post/{idPost}")

    public List<ImageDto> uploadMultipleFiles(@PathVariable Long idPost, @RequestParam("files") MultipartFile[] files) {
        return Arrays.asList(files)
                .stream()
                .map(file -> uploadFile(idPost, file)) // Gọi uploadFile cho từng ảnh
                .collect(Collectors.toList());
    }

    // API lấy danh sách hình ảnh dạng byte cho chỉnh sửa tài liệu
    @ApiOperation(value = "Lấy danh sách hình ảnh của một tài liệu khi chỉnh sửa tài liệu")
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

    // API lấy danh sách link hình ảnh của tài liệu
    @ApiOperation(value = "Lấy danh sách hình ảnh của một tài liệu khi xem chi tiết tài liệu")
    @GetMapping("/image/post/{idPost}")
    public List<String> getImageByIdPost(@PathVariable Long idPost) {
        return imageService.getImageByIdPost(idPost);
    }
}
