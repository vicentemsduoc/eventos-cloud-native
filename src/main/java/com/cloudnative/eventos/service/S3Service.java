package com.cloudnative.eventos.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface S3Service {

    String uploadFile(String folder, MultipartFile file) throws IOException;

    byte[] downloadFile(String key);

    void deleteFile(String key);

    List<String> listFiles(String prefix);

    String moveOrRenameFile(String sourceKey, String destinationKey);
}