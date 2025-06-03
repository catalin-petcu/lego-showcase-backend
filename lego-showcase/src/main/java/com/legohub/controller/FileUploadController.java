package com.legohub.controller;

import com.legohub.dto.response.ErrorResponse;
import com.legohub.dto.response.UploadResponse;
import com.legohub.service.ImageUploadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/upload")
public class FileUploadController {
    private final ImageUploadService imageUploadService;

    public FileUploadController(ImageUploadService imageUploadService) {
        this.imageUploadService = imageUploadService;
    }

    @PostMapping("/image")
    public ResponseEntity<Object> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(new ErrorResponse("Please select a file to upload"));
            }

            String filename = imageUploadService.uploadImage(file);

            return ResponseEntity.ok(new UploadResponse(
                    "Image uploaded successfully",
                    filename,
                    "/images/" + filename
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Failed to upload image: " + e.getMessage()));
        }
    }
}
