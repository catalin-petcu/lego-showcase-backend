package com.legohub.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class ImageUploadService {
    private final String uploadDir = "uploads/lego-images/";

    public ImageUploadService() {
        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to create upload directory: " + uploadDir, e);
        }
    }

    public String uploadImage(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("Please select a file to upload");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new RuntimeException("Please select a valid image file");
        }

        if (file.getSize() > 5 * 1024 * 1024) { // 5 MB limit
            throw new RuntimeException("File size exceeds the maximum limit of 5 MB");
        }

        try {
            String originalFilename = file.getOriginalFilename();
            assert originalFilename != null;
            String fileExtenstion = originalFilename.substring(originalFilename.lastIndexOf("."));
            String uniqueFilename = UUID.randomUUID() + fileExtenstion;

            Path targetPath = Paths.get(uploadDir + uniqueFilename);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            return "/images/" + uniqueFilename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image: " + e.getMessage());
        }
    }
}
