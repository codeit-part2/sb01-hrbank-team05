package com.codeit.demo.mapper;

import com.codeit.demo.dto.data.DiffDto;
import com.codeit.demo.entity.ChangeDescription;
import com.codeit.demo.entity.enums.PropertyName;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-20T18:11:26+0900",
    comments = "version: 1.6.3, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.12.1.jar, environment: Java 17.0.14 (Oracle Corporation)"
)
@Component
public class ChangeDescriptionMapperImpl implements ChangeDescriptionMapper {

    @Override
    public DiffDto toDto(ChangeDescription changeDescription) {
        if ( changeDescription == null ) {
            return null;
        }

        PropertyName propertyName = null;
        String before = null;
        String after = null;

        propertyName = changeDescription.getPropertyName();
        before = changeDescription.getBefore();
        after = changeDescription.getAfter();

        DiffDto diffDto = new DiffDto( propertyName, before, after );

        return diffDto;
    }
}
