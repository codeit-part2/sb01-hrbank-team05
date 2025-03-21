package com.codeit.demo.storage;

import com.codeit.demo.entity.BinaryContent;
import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LocalBinaryContentStorage implements BinaryContentStorage{
  private Path root = Paths.get(System.getProperty("user.dir"), "hrBank05");
  private final Path fileDir = root.resolve("file");
  private final Path backupDir = root.resolve("backup");

  private Path reosolveFilePath(Long id) {
    return fileDir.resolve(id.toString());
  }


  @PostConstruct
  public void init() {
    try {
      if (!Files.exists(root)) {
        Files.createDirectories(root);
      }
      // 파일 저장 디렉토리 생성
      if (!Files.exists(fileDir)) {
        Files.createDirectories(fileDir);
      }
      // 백업 디렉토리 생성 (필요한 경우)
      if (!Files.exists(backupDir)) {
        Files.createDirectories(backupDir);
      }
      log.info("저장소 디렉토리 초기화 완료: {}", root.toAbsolutePath());
    } catch (IOException e) {
      throw new RuntimeException("폴더 초기화 실패", e);
    }
  }


  @Override
  public Long put(Long id, byte[] content) {
    Path filePath = reosolveFilePath(id);
    try {
      Files.write(filePath, content, StandardOpenOption.CREATE_NEW);
      log.info("저장: {}", filePath.toAbsolutePath());
      return id;
    } catch (FileAlreadyExistsException e) {
      throw new RuntimeException("파일이 이미 존재합니다"+ id, e);
    } catch (IOException e) {
      throw new RuntimeException("파일 업로드 실패", e);
    }
  }

  @Override
  public InputStream get(Long id) {
    Path filePath = reosolveFilePath(id);
    try{
      return Files.newInputStream(filePath);
    }catch (IOException e){
      throw new RuntimeException("파일을 찾을 수 없습니다.",e);
    }
  }

  @Override
  public ResponseEntity<?> download(BinaryContent binaryContent) {
    Path filePath = reosolveFilePath(binaryContent.getId());
    File file = filePath.toFile();
    if(!file.exists()){
      return ResponseEntity.notFound().build();
    }
    try {
      InputStreamResource resource = new InputStreamResource(Files.newInputStream(filePath));
      return ResponseEntity.ok()
          .contentType(MediaType.parseMediaType(binaryContent.getFileFormat()))
          .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\""+binaryContent.getFileName()+"\"")
          .body(resource);
    }catch (IOException e){
      throw new RuntimeException("파일 다운로드를 실패하였습니다.",e);
    }
  }


  @Override
  public void delete(Long id) {
    Path filePath = reosolveFilePath(id);
    try {
      if (Files.exists(filePath)) {
        Files.delete(filePath);
      }
    } catch (IOException e) {
      throw new RuntimeException("Failed to delete binary content for id " + id, e);
    }
  }

}
