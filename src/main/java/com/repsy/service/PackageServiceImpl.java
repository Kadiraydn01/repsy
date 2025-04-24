package com.repsy.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.repsy.dto.MetaDTO;
import com.repsy.model.PackageEntity;
import com.repsy.repository.PackageRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class PackageServiceImpl implements PackageService {

    private final PackageRepository packageRepository;
    private final ObjectMapper objectMapper;

    private final String basePath = "storage";

    public PackageServiceImpl(PackageRepository packageRepository, ObjectMapper objectMapper) {
        this.packageRepository = packageRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void savePackage(String name, String version, MultipartFile metaFile, MultipartFile packageFile) {
        try {
            MetaDTO meta = objectMapper.readValue(metaFile.getBytes(), MetaDTO.class);


            if (!meta.getName().equals(name) || !meta.getVersion().equals(version)) {
                throw new RuntimeException("meta.json adı veya versiyonu URL ile uyuşmuyor.");
            }


            String dirPath = System.getProperty("user.dir") + File.separator + basePath + File.separator + name + File.separator + version + File.separator;
            File dir = new File(dirPath);
            if (!dir.exists()) {
                boolean created = dir.mkdirs();
                if (!created) {
                    throw new RuntimeException("Klasör oluşturulamadı: " + dirPath);
                }
            }


            String metaPath = dirPath + "meta.json";
            String packagePath = dirPath + "package.rep";


            metaFile.transferTo(new File(metaPath));
            packageFile.transferTo(new File(packagePath));

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