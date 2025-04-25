package com.repsy.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.repsy.dto.MetaDTO;
import com.repsy.model.PackageEntity;
import com.repsy.repository.PackageRepository;
import com.repsy.storage.filesystem.FileSystemStorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class PackageServiceImpl implements PackageService {

    private final PackageRepository packageRepository;
    private final ObjectMapper objectMapper;
    private final FileSystemStorageService fileSystemStorageService;

    public PackageServiceImpl(PackageRepository packageRepository, ObjectMapper objectMapper) {
        this.packageRepository = packageRepository;
        this.objectMapper = objectMapper;

        // 📂 Uygulamanın bulunduğu klasörün içine yazmak için absolute path
        String basePath = System.getProperty("user.dir") + File.separator + "storage";
        this.fileSystemStorageService = new FileSystemStorageService(basePath);
    }

    @Override
    public void savePackage(String name, String version, MultipartFile metaFile, MultipartFile packageFile) {
        try {
            MetaDTO meta = objectMapper.readValue(metaFile.getBytes(), MetaDTO.class);

            if (!meta.getName().equals(name) || !meta.getVersion().equals(version)) {
                throw new RuntimeException("meta.json adı veya versiyonu URL ile uyuşmuyor.");
            }

            String metaPath = fileSystemStorageService.store(name, version, "meta.json", metaFile);
            String packagePath = fileSystemStorageService.store(name, version, "package.rep", packageFile);

            PackageEntity entity = new PackageEntity();
            entity.setName(name);
            entity.setVersion(version);
            entity.setAuthor(meta.getAuthor());
            entity.setMetaFilePath(metaPath);
            entity.setPackageFilePath(packagePath);

            packageRepository.save(entity);

        } catch (IOException e) {
            throw new RuntimeException("Dosya işlemi sırasında hata oluştu", e);
        }
    }
}