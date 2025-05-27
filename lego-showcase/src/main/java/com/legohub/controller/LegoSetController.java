package com.legohub.controller;

import com.legohub.model.LegoSet;
import com.legohub.model.User;
import com.legohub.service.LegoSetService;
import com.legohub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/legosets")
public class LegoSetController {
    private final LegoSetService legoSetService;
    private final UserService userService;

    @Autowired
    public LegoSetController(LegoSetService legoSetService, UserService userService) {
        this.legoSetService = legoSetService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<LegoSet>> getAllSets() {
        List<LegoSet> legoSets = legoSetService.getAllLegoSets();
        return new ResponseEntity<>(legoSets, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<LegoSet> addLegoSet(@RequestBody LegoSet legoSet, @RequestParam Long userId) {
        LegoSet savedLegoSet = legoSetService.saveLegoSetIfNotExists(legoSet);
        User user = userService.getUserById(userId);
        legoSetService.addLegoSetToUser(user, legoSet);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedLegoSet);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Set<LegoSet>> getUserLegoSets(@PathVariable("userId") Long userId) {
        User user = userService.getUserById(userId);
        Set<LegoSet> legoSets = legoSetService.getLegoSetsByUser(user);
        if (legoSets.isEmpty()) {
            return ResponseEntity.ok(new HashSet<>());
        }
        return ResponseEntity.ok(legoSets);
    }
}
