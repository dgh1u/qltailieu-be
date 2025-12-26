package com.kltn.controller;

import com.kltn.config.JwtConfig;
import com.kltn.dto.entity.PostDto;
import com.kltn.dto.request.post.CreatePostRequest;
import com.kltn.dto.request.post.GetPostRequest;
import com.kltn.dto.request.post.UpdatePostRequest;
import com.kltn.dto.response.BaseResponse;
import com.kltn.dto.response.Response;
import com.kltn.dto.response.post.UpdatePostResponse;
import com.kltn.mapper.PostMapper;
import com.kltn.model.Post;
import com.kltn.repository.PostRepository;
import com.kltn.service.impl.PostServiceImp;
import com.kltn.service.impl.UserDetailServiceImp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Api(value = "Tìm nhà trọ")
public class PostController {
    private final JwtConfig jwtConfig;

    private final UserDetailServiceImp userDetailServiceImp;

    private final PostServiceImp postService;
    private final PostRepository postRepository;
    private final PostMapper postMapper;

    // API test hello world
    @GetMapping("/post/hello-world")
    public String HelloWorld() {
        return "Hello World";
    }

    // API lấy danh sách tất cả tài liệu với phân trang và bộ lọc
    @ApiOperation(value = "Lấy tất cả tài liệu")
    @GetMapping("/posts")
    public ResponseEntity<?> getAllPost(@Valid @ModelAttribute GetPostRequest request) {
        Page<Post> page = postService.getAllPost(request, PageRequest.of(request.getStart(), request.getLimit()));
        return BaseResponse.successListData(
                page.getContent().stream().map(postMapper::toPostDto).collect(Collectors.toList()),
                (int) page.getTotalElements());
    }

    // API lấy chi tiết tài liệu theo ID
    @ApiOperation(value = "Lấy thông tin của một tài liệu")
    @GetMapping("/post/{id}")
    public ResponseEntity<?> getPostById(@PathVariable Long id) {
        try {
            PostDto postDto = postService.getPostById(id);
            return BaseResponse.successData(postDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // API tạo tài liệu mới
    @ApiOperation(value = "Đăng tin mới")
    @PostMapping("/post")
    public ResponseEntity<?> createPost(@RequestHeader("Authorization") String token,
            @RequestBody @Valid CreatePostRequest createPostRequest) {
        try {

            String userId = jwtConfig.getUserIdFromJWT(token.split(" ")[1]);
            UserDetails userDetails = userDetailServiceImp.loadUserByUsername(userId);

            return BaseResponse.successData(postService.createPost(createPostRequest, userDetails.getUsername()));
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response<>("Lỗi không xác định: " + e.getMessage(), null,
                            HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    // API duyệt hoặc khóa tài liệu
    @ApiOperation(value = "Duyệt/Khóa tài liệu")
    @PutMapping("/post/{id}/approve/{bool}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> approvePostAndLogging(@RequestHeader("Authorization") String token,
            @PathVariable Long id,
            @PathVariable boolean bool) {
        try {
            String userId = jwtConfig.getUserIdFromJWT(token.split(" ")[1]);
            return BaseResponse.successData(postService.ApprovePost(id, userId, bool)); // Trả về status 200 nếu duyệt
                                                                                        // hoặc khóa thành công
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response<>("Lỗi không xác định: " + e.getMessage(), null,
                            HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    // API cập nhật thông tin tài liệu
    @ApiOperation(value = "Cập nhật một tài liệu")
    @PutMapping("/post/{id}")
    public ResponseEntity<?> updatePost(@RequestHeader("Authorization") String token, @PathVariable Long id,
            @RequestBody UpdatePostRequest updatePostRequest) {
        try {
            String userId = jwtConfig.getUserIdFromJWT(token.split(" ")[1]);
            UpdatePostResponse updatedPost = postService.updatePost(id, updatePostRequest, userId);
            return BaseResponse.successData(updatedPost);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // API ẩn hoặc hiển thị tài liệu
    @ApiOperation(value = "Ẩn/Mở khóa một tài liệu")
    @PutMapping("/post/hide/{id}")
    public ResponseEntity<?> hidePost(@PathVariable Long id) {
        return BaseResponse.successData(postService.hidePost(id));
    }

    // API xóa tài liệu bởi Admin
    @ApiOperation(value = "Xóa một tài liệu")
    @DeleteMapping("/post/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> deletePostByAdmin(@PathVariable Long id) {
        return BaseResponse.successData(postService.deletePostByAdmin(id));
    }

    // API lấy danh sách tài liệu theo người dùng
    @ApiOperation(value = "Lấy danh sách tài liệu của một người dùng")
    @GetMapping("/posts/{idUser}")
    public ResponseEntity<?> getPostsByUser(@PathVariable Long idUser, @Valid @ModelAttribute GetPostRequest request) {
        // Gán idUser vào bộ lọc (đảm bảo GetPostRequest có trường userId hoặc chuyển
        // sang PostFilterParam nếu cần)
        request.setUserId(idUser);

        // Gọi service với bộ lọc đã thiết lập
        Page<Post> page = postService.getAllPost(request, PageRequest.of(request.getStart(), request.getLimit()));

        // Chuyển đổi và trả về kết quả
        List<PostDto> postDtos = page.getContent().stream()
                .map(postMapper::toPostDto)
                .collect(Collectors.toList());

        return BaseResponse.successListData(postDtos, (int) page.getTotalElements());
    }

}
