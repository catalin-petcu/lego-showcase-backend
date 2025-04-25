package com.legohub.controller;

import com.legohub.model.LegoSet;
import com.legohub.service.LegoSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/legosets")
public class LegoSetController {
    private final LegoSetService legoSetService;

    @Autowired
    public LegoSetController(LegoSetService legoSetService) {
        this.legoSetService = legoSetService;
    }

    @GetMapping
    public ResponseEntity<List<LegoSet>> getAllSets() {
        List<LegoSet> legoSets = legoSetService.getAllLegoSets();
        return new ResponseEntity<>(legoSets, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<LegoSet> addLegoSet(@RequestBody LegoSet legoSet) {
        legoSetService.addLegoSet(legoSet);
        return new ResponseEntity<>(legoSet, HttpStatus.CREATED);
    }
}
