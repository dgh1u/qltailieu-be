package com.kltn.mapper;

import com.kltn.dto.entity.ActionDto;
import com.kltn.model.Action;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ActionMapper {

    // Giả sử User entity có field là email,
    // và bạn muốn hiển thị email vào ActionDto.username
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "fullName", source = "user.fullName")

    // Tương tự, Post có field title -> map sang postTitle
    @Mapping(target = "postTitle", source = "post.title")

    @Mapping(target = "isRead", source = "isRead")
    // Lấy luôn postId
    @Mapping(target = "postId", source = "post.id")

    // Các field còn lại (id, action, time) MapStruct tự map theo tên trùng khớp
    ActionDto toActionDto(Action action);
}
