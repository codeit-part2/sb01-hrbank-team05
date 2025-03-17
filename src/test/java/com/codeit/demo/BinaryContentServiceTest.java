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
import java.util.Comparator;

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

    @Test
    void save() {

        // 1️⃣ 파일 데이터 생성
        byte[] fileData = "테스트 파일 데이터2".getBytes();
        BinaryContent binaryContent = new BinaryContent("test2.txt", (int) fileData.length, "text/plain");

        // 2️⃣ 파일 저장 (DB + 로컬 저장소)
        BinaryContent savedBinaryContent = binaryContentService.create(fileData);

        // 3️⃣ 저장 후 ID가 생성되었는지 확인
        assertThat(savedBinaryContent.getId()).isNotNull();

        // 4️⃣ DB에서 데이터 조회
        em.flush();
        em.clear(); // 영속성 컨텍스트 초기화 후 조회
        BinaryContent foundContent = binaryContentRepository.findById(savedBinaryContent.getId())
                .orElseThrow(() -> new RuntimeException("저장된 파일을 찾을 수 없습니다."));

        // 5️⃣ 검증
        assertThat(foundContent.getFileName()).isEqualTo("test2.txt");
        assertThat(foundContent.getFileSize()).isEqualTo(fileData.length);
        assertThat(foundContent.getFileFormat()).isEqualTo("text/plain");
    }

}


