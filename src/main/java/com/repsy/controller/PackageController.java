package com.repsy.controller;

import com.repsy.model.PackageEntity;
import com.repsy.repository.PackageRepository;
import com.repsy.service.PackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class PackageController {

    private final PackageService packageService;
    private final PackageRepository packageRepository;


    @PostMapping("{name}/{version}")
    public ResponseEntity<String> uploadPackage(
            @PathVariable String name,
            @PathVariable String version,
            @RequestParam("metaFile") MultipartFile metaFile,
            @RequestParam("packageFile") MultipartFile packageFile) {

        packageService.savePackage(name, version, metaFile, packageFile);
        return ResponseEntity.ok("Paket başarıyla yüklendi.");
    }


    @GetMapping("{name}/{version}/{fileName}")
    public ResponseEntity<?> downloadFile(
            @PathVariable String name,
            @PathVariable String version,
            @PathVariable String fileName) {

        Optional<PackageEntity> optionalPackage = packageRepository.findByNameAndVersion(name, version);

        if (optionalPackage.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        PackageEntity pkg = optionalPackage.get();
        String path;

        if (fileName.equals("meta.json")) {
            path = pkg.getMetaFilePath();
        } else if (fileName.equals("package.rep")) {
            path = pkg.getPackageFilePath();
        } else {
            return ResponseEntity.badRequest().body("Geçersiz dosya ismi.");
        }

        try {
            File file = new File(path);
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName)
                    .contentLength(file.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);

        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Dosya okunamadı.");
        }
    }
}