package com.kltn.service.impl;

import com.kltn.dto.entity.*;
import com.kltn.dto.response.post.*;
import com.kltn.model.*;
import com.kltn.repository.*;
import com.kltn.dto.entity.*;
import com.kltn.dto.request.post.CreatePostRequest;
import com.kltn.dto.request.post.UpdatePostRequest;
import com.kltn.dto.response.post.*;
import com.kltn.exception.DataNotFoundException;
import com.kltn.mapper.CriteriaMapper;
import com.kltn.mapper.CommentMapper;
import com.kltn.mapper.PostMapper;
import com.kltn.mapper.UserMapper;
import com.kltn.model.*;
import com.kltn.model.enums.ActionName;
import com.kltn.repository.*;
import com.kltn.repository.custom.CustomPostQuery;
import com.kltn.service.PostService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImp implements PostService {
    // Inject Service
    private final ApplicationEventPublisher applicationEventPublisher;
    // Inject Repository into class
    private final PostRepository postRepository;

    private final UserRepository userRepository;

    private final CriteriaRepository criteriaRepository;

    private final CommentRepository commentRepository;

    private final ImageServiceImp imageServiceImp;

    private final DocumentServiceImpl documentServiceImpl;

    private final ActionServiceImp actionService;

    private final PostMapper postMapper;

    private final CriteriaMapper criteriaMapper;

    private final UserMapper userMapper;

    private final CommentMapper commentMapper;

    // Lấy danh sách tất cả tài liệu theo bộ lọc và phân trang
    @Override
    public Page<Post> getAllPost(CustomPostQuery.PostFilterParam param, PageRequest pageRequest) {
        try {
            Specification<Post> specification = CustomPostQuery.getFilterPost(param);
            return postRepository.findAll(specification, pageRequest);
        } catch (Exception e) {
            throw new DataNotFoundException("Không có tài liệu nào được tìm thấy! " + e.getMessage());
        }
    }

    // Lấy chi tiết tài liệu theo ID kèm theo comments, images, documents
    @Override
    public PostDto getPostById(Long id) {
        // Tìm tài liệu
        Optional<Post> post = postRepository.findPostById(id);

        // Kiểm tra xem tài liệu có tồn tại không
        if (post.isPresent()) {
            PostDto postDto = postMapper.toPostDto(post.get());
            // Lay cho o ra
            CriteriaDto criteriaDto = criteriaMapper.toCriteriaDto(post.get().getCriteria());
            // Lấy các bình luận của tài liệu
            List<CommentDto> commentDtos = new ArrayList<>();
            List<Comment> comments = commentRepository.findCommentsByPostId(id);
            for (Comment comment : comments) {
                commentDtos.add(commentMapper.toCommentDTO(comment));
            }
            // Lấy hình ảnh của tài liệu
            List<String> images = imageServiceImp.getImageByIdPost(id);

            List<DocumentDto> documents = documentServiceImpl.getDocumentDTOsByIdPost(id);
            postDto.setDocuments(documents);

            // Thiết lập dữ liệu cho DTO
            postDto.setCriteriaDTO(criteriaDto);
            postDto.setImageStrings(images);
            postDto.setCommentDTOS(commentDtos);
            postDto.setUserDTO(userMapper.toUserDto(post.get().getUser()));

            // Trả về thông tin tài liệu
            return postDto;
        } else {
            // Nếu không tìm thấy tài liệu
            throw new DataNotFoundException("Không tìm thấy tài liệu theo id đã cho");
        }
    }

    // Tạo tài liệu mới và lưu vào database
    @Override
    @Transactional
    public CreatePostResponse createPost(CreatePostRequest createPostRequest, String email) {
        try {
            Optional<User> userOptional = userRepository.findByEmail(email);
            if (userOptional.isEmpty()) {
                throw new DataNotFoundException("User not found with email: " + email);
            }
            User user = userOptional.get();

            userRepository.save(user);

            // Tạo tài liệu mới
            Post post = postMapper.createRequestDtoToPost(createPostRequest);
            post.setCreateAt(LocalDateTime.now());
            post.setLastUpdate(LocalDateTime.now());
            post.setUser(user);
            post.setDel(false);
            post.setApproved(true);
            post.setNotApproved(true);

            // Xử lý đối tượng Criteria liên quan đến tài liệu
            Criteria criteria = criteriaMapper.toCriteria(createPostRequest.getCriteria());
            criteria.setId(null);
            criteria.setPost(post);
            Criteria criteriaSaved = criteriaRepository.save(criteria);
            post.setCriteria(criteriaSaved);

            // Lưu tài liệu vào database
            Post postSaved = postRepository.save(post);

            // Tạo action cho tài liệu
            actionService.createAction(post, user, ActionName.CREATE);

            return postMapper.toCreatePostResponse(postSaved);
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // Cập nhật thông tin tài liệu và criteria
    @Override
    @Transactional
    public UpdatePostResponse updatePost(Long id, UpdatePostRequest updatePostRequest, String userId) {
        try {
            // Kiểm tra xem tài liệu có tồn tại không
            Post post = postRepository.findPostById(id)
                    .orElseThrow(() -> new DataNotFoundException("Không tìm thấy tài liệu với ID: " + id));

            // Cập nhật thông tin Criteria
            Criteria criteria = criteriaMapper.toCriteria(updatePostRequest.getCriteria());

            // Cập nhật thông tin Post
            post.setTitle(updatePostRequest.getTitle());
            post.setContent(updatePostRequest.getContent());
            post.setLastUpdate(LocalDateTime.now());
            post.setCriteria(criteria);
            post.setApproved(true);
            post.setNotApproved(true);

            // Gán Criteria vào Post (quan hệ 1-1)
            criteria.setPost(post);

            // Lưu vào database
            criteriaRepository.save(criteria);
            postRepository.save(post);

            return postMapper.toUpdatePostResponse(post);
        } catch (Exception e) {
            log.error("Lỗi khi cập nhật tài liệu: {}", e.getMessage());
            throw new RuntimeException("Lỗi trong quá trình cập nhật tài liệu: " + e.getMessage());
        }
    }

    // Ẩn hoặc hiện tài liệu
    @Override
    public HiddenPostResponse hidePost(Long id) {
        try {
            // Tìm tài liệu, nếu không có thì ném DataNotFoundException
            Post post = postRepository.findById(id)
                    .orElseThrow(() -> new DataNotFoundException("Không tìm thấy tài liệu với ID " + id));

            // Chuyển đổi trạng thái của thuộc tính del (nếu false -> true, nếu true ->
            // false)
            post.setDel(!post.getDel());
            postRepository.save(post);

            // Tạo thông báo phù hợp dựa trên trạng thái mới của del
            String statusMessage = post.getDel() ? "tài liệu đã được ẩn thành công."
                    : "tài liệu đã được hiển thị thành công.";
            return new HiddenPostResponse(post.getId(), statusMessage, post.getDel());
        } catch (DataNotFoundException e) {
            log.warn("Không tìm thấy tài liệu với ID: {}", id);
            throw e; // Ném lỗi tiếp để controller xử lý
        } catch (Exception e) {
            log.error("Lỗi khi ẩn/bật tài liệu ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Đã xảy ra lỗi khi ẩn/bật tài liệu.");
        }
    }

    // Xóa tài liệu bởi Admin
    @Override
    public DeletePostResponse deletePostByAdmin(Long id) {
        try {
            // Tìm tài liệu, nếu không có thì ném DataNotFoundException
            Post post = postRepository.findById(id)
                    .orElseThrow(() -> new DataNotFoundException("Không tìm thấy tài liệu với ID " + id));

            // Xóa tài liệu
            postRepository.delete(post);

            // Trả về response
            return new DeletePostResponse(id, "tài liệu đã bị xóa bởi Admin.", true);
        } catch (DataNotFoundException e) {
            log.warn("Không tìm thấy tài liệu với ID: {}", id);
            throw e; // Ném lỗi để controller xử lý
        } catch (Exception e) {
            log.error("Lỗi khi Admin xóa tài liệu ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Đã xảy ra lỗi khi xóa tài liệu.");
        }
    }

    // Duyệt hoặc khóa tài liệu
    @Override
    public ApprovePostResponse ApprovePost(Long idPost, String usernameApprove, boolean isApprove) {
        try {
            Optional<Post> postOpt = postRepository.findById(idPost);
            if (postOpt.isEmpty()) {
                return new ApprovePostResponse(idPost, "Không tìm thấy tài liệu", false);
            }

            Optional<User> userOpt = userRepository.findByEmail(usernameApprove);
            if (userOpt.isEmpty()) {
                return new ApprovePostResponse(idPost, "Không tìm thấy người dùng có username: " + usernameApprove,
                        false);
            }

            Post post = postOpt.get();
            User user = userOpt.get();
            User postOwner = post.getUser(); // Chủ tài liệu

            if (isApprove) {
                // Duyệt tài liệu
                post.setApproved(true);
                post.setNotApproved(false);
                actionService.createAction(post, user, ActionName.APPROVE);
            } else {
                // Khóa tài liệu
                // Kiểm tra trạng thái hiện tại của tài liệu
                boolean wasWaitingApproval = post.getApproved() && post.getNotApproved(); // Chờ duyệt
                boolean wasApproved = post.getApproved() && !post.getNotApproved(); // Đã duyệt

                // Cập nhật trạng thái khóa bài
                post.setApproved(false);
                post.setNotApproved(true);

                actionService.createAction(post, user, ActionName.BLOCK);
            }

            postRepository.save(post);

            String message = "tài liệu đã được " + (isApprove ? "duyệt" : "khóa") + " thành công";

            return new ApprovePostResponse(idPost, message, isApprove);

        } catch (Exception e) {
            log.error("Lỗi khi duyệt tài liệu: {}", e.getMessage());
            return new ApprovePostResponse(idPost, "Đã xảy ra lỗi trong quá trình xử lý", false);
        }
    }

}
