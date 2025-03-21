package com.codeit.demo.service;

import com.codeit.demo.entity.BinaryContent;
import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface BinaryContentService {

  BinaryContent createBinaryContent(MultipartFile file) throws IOException;

  BinaryContent findById(Long id);

  void delete(Long id);

}