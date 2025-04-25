package com.repsy.storage.filesystem;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileSystemStorageService {

    private final String basePath;

    public FileSystemStorageService(String basePath) {
        this.basePath = basePath;
    }

    public String store(String packageName, String version, String fileName, MultipartFile file) throws IOException {
        String dirPath = basePath + File.separator + packageName + File.separator + version;
        File dir = new File(dirPath);

        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            if (!created) {
                throw new IOException("Klasör oluşturulamadı: " + dirPath);
            }
        }

        String fullPath = dirPath + File.separator + fileName;
        File targetFile = new File(fullPath);
        file.transferTo(targetFile);

        return fullPath;
    }

    public byte[] load(String packageName, String version, String fileName) throws IOException {
        String fullPath = basePath + File.separator + packageName + File.separator + version + File.separator + fileName;
        File file = new File(fullPath);
        return Files.readAllBytes(file.toPath());
    }
}