package com.legohub.controller;

import com.legohub.dto.response.ErrorResponse;
import com.legohub.dto.response.ImageUploadReponse;
import com.legohub.dto.response.LegoSetReponse;
import com.legohub.model.LegoSet;
import com.legohub.model.User;
import com.legohub.service.ImageUploadService;
import com.legohub.service.LegoSetService;
import com.legohub.service.UserService;
import com.legohub.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/lego-sets")
public class LegoSetController {
    private final LegoSetService legoSetService;
    private final UserService userService;
    private final ImageUploadService imageUploadService;

    @Autowired
    public LegoSetController(LegoSetService legoSetService, UserService userService, ImageUploadService imageUploadService) {
        this.legoSetService = legoSetService;
        this.userService = userService;
        this.imageUploadService = imageUploadService;
    }

    @PostMapping
    public ResponseEntity<Object> addLegoSet(@RequestBody Map<String, String> request) {
        try {
            User currentUser = userService.getCurrentAuthenticatedUser();
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            String setNumber = request.get("setNumber");
            if (StringUtils.isNullOrEmpty(setNumber)) {
                return ResponseEntity.badRequest().body(new ErrorResponse("Set number is required"));
            }

            LegoSet legoSet = legoSetService.validateAndSaveLegoSet(setNumber);

            legoSetService.addLegoSetToUser(currentUser, legoSet);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new LegoSetReponse("LEGO set added successfully", legoSet));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/my-sets")
    public ResponseEntity<Object> getMyLegoSets() {
        try {
            User currentUser = userService.getCurrentAuthenticatedUser();
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            Set<LegoSet> legoSets = legoSetService.getLegoSetsByUser(currentUser);
            return ResponseEntity.ok(legoSets);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Failed to retrieve LEGO sets: " + e.getMessage()));
        }
    }

    @PostMapping("/{setId}/upload-image")
    public ResponseEntity<Object> uploadImage(@PathVariable String setId, @RequestParam("image") MultipartFile file) {
        try {
            User currentUser = userService.getCurrentAuthenticatedUser();
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            String relativeImageUrl = imageUploadService.uploadImage(file);
            String fullImageUrl = "http://localhost:8080" + relativeImageUrl;
            LegoSet updatedSet = legoSetService.updateSetImage(Long.valueOf(setId), fullImageUrl, currentUser);

            return ResponseEntity.ok(new ImageUploadReponse(
                    "Image uploaded successfully",
                    fullImageUrl,
                    updatedSet
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }
}
