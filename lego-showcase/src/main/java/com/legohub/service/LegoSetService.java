package com.legohub.service;

import com.legohub.model.LegoSet;
import com.legohub.model.User;
import com.legohub.model.UserLegoSet;
import com.legohub.repository.LegoSetRepository;
import com.legohub.repository.UserLegoSetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class LegoSetService {
    private final LegoSetRepository legoSetRepository;
    private final UserLegoSetRepository userLegoSetRepository;

    @Autowired
    public LegoSetService(LegoSetRepository legoSetRepository,
                          UserLegoSetRepository userLegoSetRepository) {
        this.legoSetRepository = legoSetRepository;
        this.userLegoSetRepository = userLegoSetRepository;
    }

    public List<LegoSet> getAllLegoSets() {
        return legoSetRepository.findAll();
    }

    public void addLegoSetToUser(User user, LegoSet legoSet) {
        UserLegoSet userLegoSet = new UserLegoSet();
        userLegoSet.setUser(user);
        userLegoSet.setLegoSet(legoSet);
        userLegoSetRepository.save(userLegoSet);
    }

    public LegoSet saveLegoSetIfNotExists(LegoSet legoSet) {
        LegoSet existingLegoSet = legoSetRepository.findById(legoSet.getSetId())
                .orElse(null);
        if (existingLegoSet != null) {
            return existingLegoSet;
        }
        return legoSetRepository.save(legoSet);
    }

    public Set<LegoSet> getLegoSetsByUser(User user) {
        List<UserLegoSet> userLegoSets = userLegoSetRepository.findByUser(user);
        Set<LegoSet> legoSets = new HashSet<>();
        for (UserLegoSet userLegoSet : userLegoSets) {
            legoSets.add(userLegoSet.getLegoSet());
        }
        return legoSets;
    }
}
