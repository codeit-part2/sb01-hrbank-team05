package com.codeit.demo.controller;

import com.codeit.demo.controller.api.BinaryContentApi;
import com.codeit.demo.entity.BinaryContent;
import com.codeit.demo.service.BinaryContentService;
import com.codeit.demo.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class BinaryContentController implements BinaryContentApi {
  private final BinaryContentService binaryContentService;
  private final BinaryContentStorage binaryContentStorage;

  @PostMapping(consumes = {"multipart/form-data"})
  public ResponseEntity<List<BinaryContent>> saveBinaryContent(@RequestParam("files") List<MultipartFile> files)
      throws IOException {
    List<BinaryContent> savedFiles = new ArrayList<>();
    for (MultipartFile file : files) {
      BinaryContent savedContent = binaryContentService.createBinaryContent(file);
      savedFiles.add(savedContent);
    }
    return ResponseEntity.ok(savedFiles);
  }

  @GetMapping("/{binaryContentId}/download")
  @Override
  public ResponseEntity<?> download(@PathVariable Long binaryContentId) {
    BinaryContent findFile = binaryContentService.findById(binaryContentId);
    return binaryContentStorage.download(findFile);
  }
}
