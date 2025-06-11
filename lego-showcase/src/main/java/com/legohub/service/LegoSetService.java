package com.legohub.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.legohub.exception.InvalidLegoNumberException;
import com.legohub.exception.LegoAlreadyExistsException;
import com.legohub.exception.NoUserLegoRelationException;
import com.legohub.model.LegoSet;
import com.legohub.model.User;
import com.legohub.model.UserLegoSet;
import com.legohub.repository.LegoSetRepository;
import com.legohub.repository.UserLegoSetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class LegoSetService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LegoSetService.class);

    private final LegoSetRepository legoSetRepository;
    private final UserLegoSetRepository userLegoSetRepository;
    private final RebrickableService rebrickableService;

    @Autowired
    public LegoSetService(LegoSetRepository legoSetRepository,
                          UserLegoSetRepository userLegoSetRepository,
                          RebrickableService rebrickableService) {
        this.legoSetRepository = legoSetRepository;
        this.userLegoSetRepository = userLegoSetRepository;
        this.rebrickableService = rebrickableService;
    }

    public LegoSet validateAndSaveLegoSet(String setNumber) {
        LegoSet existingSet = legoSetRepository.findBySetNumber(setNumber);
        if (existingSet != null) {
            LOGGER.info("Found existing Lego set: {}", existingSet.getName());
            return existingSet;
        }

        try {
            JsonNode rebrickableData = rebrickableService.getSetByNumber(setNumber);

            String name = rebrickableData.get("name").asText();

            int themeId = rebrickableData.get("theme_id").asInt();
            String collection = rebrickableService.getThemeName(themeId);

            int pieceCount = rebrickableData.get("num_parts").asInt();
            Integer year = rebrickableData.has("year") ? rebrickableData.get("year").asInt() : null;

            LegoSet newSet = new LegoSet(setNumber, name, collection, pieceCount, year);
            LegoSet savedSet = legoSetRepository.save(newSet);

            LOGGER.info("Saved new Lego set: {}", savedSet.getName());
            return savedSet;
        } catch (Exception e) {
            throw new InvalidLegoNumberException("Invalid Lego set number: " + setNumber);
        }
    }

    public void addLegoSetToUser(User user, LegoSet legoSet) {
        UserLegoSet existingUserLegoSet = userLegoSetRepository.findByUserAndLegoSet(user, legoSet);
        if (existingUserLegoSet != null) {
            throw new LegoAlreadyExistsException("Lego set already exists: " + legoSet.getName());
        }

        UserLegoSet userLegoSet = new UserLegoSet();
        userLegoSet.setUser(user);
        userLegoSet.setLegoSet(legoSet);
        userLegoSetRepository.save(userLegoSet);
    }

    public Set<LegoSet> getLegoSetsByUser(User user) {
        List<UserLegoSet> userLegoSets = userLegoSetRepository.findByUser(user);
        Set<LegoSet> legoSets = new HashSet<>();
        for (UserLegoSet userLegoSet : userLegoSets) {
            legoSets.add(userLegoSet.getLegoSet());
        }
        return legoSets;
    }

    public LegoSet updateSetImage(Long setId, String imageUrl, User user) {
        LegoSet legoSet = legoSetRepository.findById(setId)
                .orElseThrow(() -> new RuntimeException("Lego set not found"));

        UserLegoSet userLegoSet = userLegoSetRepository.findByUserAndLegoSet(user, legoSet);
        if (userLegoSet == null) {
            throw new NoUserLegoRelationException("User does not own this Lego set");
        }

        legoSet.setUserImageUrl(imageUrl);
        LegoSet savedSet = legoSetRepository.save(legoSet);

        LOGGER.info("Updated Lego set image: {} with URL: {}", savedSet.getName(), imageUrl);
        return savedSet;
    }
}
