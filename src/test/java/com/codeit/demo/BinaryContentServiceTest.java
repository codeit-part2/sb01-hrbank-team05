package com.codeit.demo;

import com.codeit.demo.entity.BinaryContent;
import com.codeit.demo.repository.BinaryContentRepository;
import com.codeit.demo.service.impl.BinaryContentServiceImpl;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.util.Comparator.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ContextConfiguration(classes = HrbankApplication.class)
@Transactional
@Rollback(false)
class BinaryContentServiceTest {
    @Autowired
    EntityManager em;
    @Autowired
    BinaryContentServiceImpl binaryContentService;
    @Autowired
    BinaryContentRepository binaryContentRepository;

    @BeforeEach
    void cleanUpFiles() throws IOException {
        Path rootPath = Paths.get(System.getProperty("user.dir"), "hrBank05");
        Path filePath = rootPath.resolve("file");
        Path backupPath = rootPath.resolve("backup");

        // 폴더가 존재하면 삭제
        if (Files.exists(rootPath)) {
            Files.walk(rootPath)
                    .sorted(reverseOrder()) // 파일부터 삭제 후 폴더 삭제
                    .map(Path::toFile)
                    .forEach(File::delete);
        }

        // 기본 폴더 재생성
        Files.createDirectories(rootPath);
        Files.createDirectories(filePath);
        Files.createDirectories(backupPath);// 빈 폴더 다시 생성
    }


}


