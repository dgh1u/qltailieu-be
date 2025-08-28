package com.kltn.mapper;

import com.kltn.dto.entity.DocumentDto;
import com.kltn.dto.entity.CriteriaDto;
import com.kltn.dto.entity.PostDto;
import com.kltn.dto.request.post.CreatePostRequest;
import com.kltn.dto.response.post.CreatePostResponse;
import com.kltn.dto.response.post.UpdatePostResponse;
import com.kltn.model.Post;
import com.kltn.model.Criteria;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class, CriteriaMapper.class, CommentMapper.class, DocumentMapper.class})
public interface PostMapper {

    @Mapping(target = "userDTO", source = "user")
    @Mapping(target = "criteriaDTO", source = "criteria")
    @Mapping(target = "documents", expression = "java(getDocumentDtos(post))")
    PostDto toPostDto(Post post);

    // Method helper để map documents
    default List<DocumentDto> getDocumentDtos(Post post) {
        // Logic để lấy documents sẽ được xử lý trong service layer
        return new ArrayList<>();
    }

    Post toPost(PostDto postDto);

    // Chuyển đổi từ CreatePostRequest sang Post
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "approved", ignore = true)
    @Mapping(target = "del", ignore = true)
    @Mapping(target = "lastUpdate", ignore = true)
    @Mapping(target = "notApproved", ignore = true)
    @Mapping(target = "createAt", ignore = true)
    @Mapping(target = "criteria", ignore = true)
    @Mapping(target = "user", ignore = true)
    Post createRequestDtoToPost(CreatePostRequest createPostRequest);

    // Fix: Kiểm tra lại property mapping
    @Mapping(target = "criteriaDTO", source = "criteria")
    @Mapping(target = "userDTO", source = "user")
    UpdatePostResponse toUpdatePostResponse(Post post);

    // Fix: Sử dụng expression để handle null safety
    @Mapping(target = "user", expression = "java(post.getUser() != null ? post.getUser().getEmail() : null)")
    @Mapping(target = "criteriaId", expression = "java(post.getCriteria() != null ? post.getCriteria().getId() : null)")
    @Mapping(target = "createAt", source = "createAt")
    @Mapping(target = "lastUpdate", source = "lastUpdate")
    CreatePostResponse toCreatePostResponse(Post post);

    // Alternative approach using conditional mapping
    // Nếu bạn muốn sử dụng cách tiếp cận khác, có thể dùng:
    /*
    @Mapping(target = "user", source = "user.email", conditionExpression = "java(post.getUser() != null)")
    @Mapping(target = "criteriaId", source = "criteria.id", conditionExpression = "java(post.getCriteria() != null)")
    @Mapping(target = "createAt", source = "createAt")
    @Mapping(target = "lastUpdate", source = "lastUpdate")
    CreatePostResponse toCreatePostResponseAlternative(Post post);
    */
}