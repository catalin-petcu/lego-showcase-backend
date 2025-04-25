package com.legohub.service;

import com.legohub.model.LegoSet;
import com.legohub.repository.LegoSetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LegoSetService {
    private final LegoSetRepository legoSetRepository;

    @Autowired
    public LegoSetService(LegoSetRepository legoSetRepository) {
        this.legoSetRepository = legoSetRepository;
    }

    public List<LegoSet> getAllLegoSets() {
        return legoSetRepository.findAll();
    }

    public void addLegoSet(LegoSet legoSet) {
        legoSetRepository.save(legoSet);
    }
}
