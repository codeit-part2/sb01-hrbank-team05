package com.codeit.demo.service.impl;

import com.codeit.demo.entity.BinaryContent;
import com.codeit.demo.exception.FileNotFoundException;
import com.codeit.demo.exception.FileStorageException;
import com.codeit.demo.repository.BinaryContentRepository;
import com.codeit.demo.service.BinaryContentService;
import com.codeit.demo.storage.BinaryContentStorage;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BinaryContentServiceImpl implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;

  @Transactional
  public BinaryContent createBinaryContent(MultipartFile file) throws IOException {

    BinaryContent binaryContent = new BinaryContent(
        file.getOriginalFilename(),
        (int) file.getSize(),
        file.getContentType()
    );

    byte[] data = file.getBytes();

    BinaryContent savedContent = binaryContentRepository.save(binaryContent);
    binaryContentStorage.put(savedContent.getId(), data);
    return savedContent;
  }

  public BinaryContent findById(Long id) {
    return binaryContentRepository.findById(id)
        .orElseThrow(() -> new FileNotFoundException("파일을 찾을 수 없습니다: " + id));
  }

  public List<BinaryContent> findAllByIdIn(List<Long> ids) {
    return binaryContentRepository.findAllByIdIn(ids);
  }

  @Transactional
  public void delete(Long id) {
    try {
      BinaryContent binaryContent = findById(id);
      binaryContentRepository.deleteById(id);
      binaryContentStorage.delete(id);
      log.info("파일이 성공적으로 삭제되었습니다: {}", binaryContent.getFileName());
    } catch (Exception ex) {
      throw new FileStorageException("파일 삭제 중 오류가 발생했습니다.", ex);
    }
  }
}