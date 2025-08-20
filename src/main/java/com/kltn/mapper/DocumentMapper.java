// src/main/java/com/nckh/motelroom/mapper/DocumentMapper.java
package com.kltn.mapper;

import com.kltn.dto.entity.DocumentDto;
import com.kltn.model.Document;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DocumentMapper {
    @Mapping(target = "idPost", source = "post.id")
    DocumentDto toDto(Document document);
}