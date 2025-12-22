package com.kltn.service;

import com.kltn.dto.entity.PostDto;
import com.kltn.dto.entity.SearchDto;
import com.kltn.dto.request.post.CreatePostRequest;
import com.kltn.dto.request.post.UpdatePostRequest;
import com.kltn.dto.response.post.*;
import com.kltn.dto.response.post.*;
import com.kltn.model.Post;
import com.kltn.repository.custom.CustomPostQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface PostService {
    Page<Post> getAllPost(CustomPostQuery.PostFilterParam param, PageRequest pageRequest);

    PostDto getPostById(Long id);

    CreatePostResponse createPost(CreatePostRequest createPostRequest, String email);

    UpdatePostResponse updatePost(Long id, UpdatePostRequest updatePostRequest, String name);

    HiddenPostResponse hidePost(Long id);

    DeletePostResponse deletePostByAdmin(Long id);

    ApprovePostResponse ApprovePost(Long idPost, String usernameApprove, boolean isApprove);

}
