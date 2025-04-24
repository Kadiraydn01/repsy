package com.repsy.service;

import org.springframework.web.multipart.MultipartFile;

public interface PackageService {
void savePackage (String name, String version, MultipartFile metafile, MultipartFile packageFile);
}
