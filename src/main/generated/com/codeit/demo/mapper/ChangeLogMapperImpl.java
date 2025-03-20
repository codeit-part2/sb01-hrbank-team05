package com.codeit.demo.mapper;

import com.codeit.demo.dto.data.ChangeLogDto;
import com.codeit.demo.entity.ChangeLog;
import java.time.Instant;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-20T18:11:26+0900",
    comments = "version: 1.6.3, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.12.1.jar, environment: Java 17.0.14 (Oracle Corporation)"
)
@Component
public class ChangeLogMapperImpl implements ChangeLogMapper {

    @Override
    public ChangeLogDto toDto(ChangeLog changeLog) {
        if ( changeLog == null ) {
            return null;
        }

        Long id = null;
        String type = null;
        String employeeNumber = null;
        String memo = null;
        String ipAddress = null;
        Instant at = null;

        id = changeLog.getId();
        if ( changeLog.getType() != null ) {
            type = changeLog.getType().name();
        }
        employeeNumber = changeLog.getEmployeeNumber();
        memo = changeLog.getMemo();
        ipAddress = changeLog.getIpAddress();
        at = changeLog.getAt();

        ChangeLogDto changeLogDto = new ChangeLogDto( id, type, employeeNumber, memo, ipAddress, at );

        return changeLogDto;
    }
}
