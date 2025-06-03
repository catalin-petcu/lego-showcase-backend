package com.legohub.repository;

import com.legohub.model.LegoSet;
import com.legohub.model.User;
import com.legohub.model.UserLegoSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserLegoSetRepository extends JpaRepository<UserLegoSet, Long> {
    List<UserLegoSet> findByUser(User user);
    UserLegoSet findByUserAndLegoSet(User user, LegoSet legoSet);
}
