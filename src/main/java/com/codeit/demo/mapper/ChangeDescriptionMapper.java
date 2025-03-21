package com.codeit.demo.mapper;

import com.codeit.demo.dto.data.DiffDto;
import com.codeit.demo.entity.ChangeDescription;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChangeDescriptionMapper {

  DiffDto toDto(ChangeDescription changeDescription);

}
