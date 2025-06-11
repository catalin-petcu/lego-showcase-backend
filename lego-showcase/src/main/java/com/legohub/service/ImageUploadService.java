package com.legohub.service;

import com.legohub.exception.*;
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
    private static final String UPLOAD_DIR = "uploads/lego-images/";

    public ImageUploadService() {
        try {
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
        } catch (Exception e) {
            throw new DirectoryCreationException("Failed to create upload directory: " + UPLOAD_DIR, e);
        }
    }

    public String uploadImage(MultipartFile file) {
        if (file.isEmpty()) {
            throw new FileEmptyException("Please select a file to upload");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new InvalidFileTypeException("Please select a valid image file");
        }

        if (file.getSize() > 5 * 1024 * 1024) { // 5 MB limit
            throw new FileSizeExceededException("File size exceeds the maximum limit of 5 MB");
        }

        try {
            String originalFilename = file.getOriginalFilename();
            assert originalFilename != null;
            String fileExtenstion = originalFilename.substring(originalFilename.lastIndexOf("."));
            String uniqueFilename = UUID.randomUUID() + fileExtenstion;

            Path targetPath = Paths.get(UPLOAD_DIR + uniqueFilename);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            return "/images/" + uniqueFilename;
        } catch (IOException e) {
            throw new FileUploadException("Failed to upload image: " + e.getMessage());
        }
    }
}
