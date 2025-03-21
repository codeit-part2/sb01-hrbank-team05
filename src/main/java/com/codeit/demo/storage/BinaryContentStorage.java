package com.codeit.demo.storage;

import com.codeit.demo.entity.BinaryContent;
import java.io.InputStream;
import org.springframework.http.ResponseEntity;

public interface BinaryContentStorage {
  Long put(Long id,byte[] content);
  InputStream get(Long id);
  ResponseEntity<?> download(BinaryContent binaryContent);
  void delete(Long id);
}
