package com.legohub.controller;

import ch.qos.logback.core.util.StringUtil;
import com.legohub.model.LegoSet;
import com.legohub.model.User;
import com.legohub.service.LegoSetService;
import com.legohub.service.UserService;
import com.legohub.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/lego-sets")
public class LegoSetController {
    private final LegoSetService legoSetService;
    private final UserService userService;

    @Autowired
    public LegoSetController(LegoSetService legoSetService, UserService userService) {
        this.legoSetService = legoSetService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> addLegoSet(@RequestBody Map<String, String> request) {
        try {
            User currentUser = userService.getCurrentAuthenticatedUser();
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            String setNumber = request.get("setNumber");
            if (StringUtils.isNullOrEmpty(setNumber)) {
                return ResponseEntity.badRequest().body(Map.of("error", "Set number is required"));
            }

            LegoSet legoSet = legoSetService.validateAndSaveLegoSet(setNumber);

            legoSetService.addLegoSetToUser(currentUser, legoSet);

            return ResponseEntity.status(HttpStatus.CREATED).body(legoSet);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/my-sets")
    public ResponseEntity<Set<LegoSet>> getMyLegoSets() {
        try {
            User currentUser = userService.getCurrentAuthenticatedUser();
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            Set<LegoSet> legoSets = legoSetService.getLegoSetsByUser(currentUser);
            return ResponseEntity.ok(legoSets);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
