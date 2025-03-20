package com.codeit.demo.mapper;

import com.codeit.demo.dto.data.ChangeLogDto;
import com.codeit.demo.entity.ChangeLog;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChangeLogMapper {

  ChangeLogDto toDto(ChangeLog changeLog);
}