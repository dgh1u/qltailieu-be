package com.kltn.mapper;

import com.kltn.dto.entity.DocumentDto;
import com.kltn.dto.entity.PostDto;
import com.kltn.dto.request.post.CreatePostRequest;
import com.kltn.dto.response.post.CreatePostResponse;
import com.kltn.dto.response.post.UpdatePostResponse;
import com.kltn.model.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class, CriteriaMapper.class, CommentMapper.class, DocumentMapper.class})
public interface PostMapper {

    @Mapping(target = "userDTO", source = "user")
    @Mapping(target = "criteriaDTO", source = "criteria")
    @Mapping(target = "documents", expression = "java(getDocumentDtos(post))")
//    @Mapping(target = "commentDTOS", source = "comments")
//    @Mapping(target = "imageStrings", expression = "java(post.getImages() != null ? post.getImages().stream().map(Image::getUrl).collect(Collectors.toList()) : Collections.emptyList())")
    PostDto toPostDto(Post post);
    // Thêm method helper để map documents
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

    @Mapping(source = "post.criteria" , target = "criteriaDTO")
    @Mapping(source = "post.user" , target = "userDTO")
    UpdatePostResponse toUpdatePostResponse(Post post);

    @Mapping(target = "user", source = "post.user.email")  // Ánh xạ từ user sang email thay vì username
    @Mapping(target = "criteriaId", source = "post.criteria.id")  // Ánh xạ từ criteria sang id
    @Mapping(target = "createAt", source = "post.createAt")  // Ánh xạ createAt
    @Mapping(target = "lastUpdate", source = "post.lastUpdate")  // Ánh xạ lastUpdate
    CreatePostResponse toCreatePostResponse(Post post);
}
