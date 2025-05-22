package com.legohub.model;

import jakarta.persistence.*;

@Entity
@Table(name = "user_lego_set")
public class UserLegoSet {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "lego_set_id")
    private LegoSet legoSet;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LegoSet getLegoSet() {
        return legoSet;
    }

    public void setLegoSet(LegoSet legoSet) {
        this.legoSet = legoSet;
    }
}
