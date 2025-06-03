package com.legohub.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class LegoSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long setId;

    @Column(unique = true, nullable = false)
    private String setNumber;

    private String name;
    private String collection;
    private int pieceCount;
    private Integer year;
    private String userImageUrl;

    @OneToMany(mappedBy = "legoSet", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<UserLegoSet> users = new HashSet<>();

    public LegoSet() {}

    public LegoSet(String setNumber, String name, String collection, int pieceCount, Integer year) {
        this.setNumber = setNumber;
        this.name = name;
        this.collection = collection;
        this.pieceCount = pieceCount;
        this.year = year;
        this.userImageUrl = null;
    }

    public Long getSetId() {
        return setId;
    }
    public void setSetId(Long setId) {
        this.setId = setId;
    }

    public String getSetNumber() {
        return setNumber;
    }
    public void setSetNumber(String setNumber) {
        this.setNumber = setNumber;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getCollection() {
        return collection;
    }
    public void setCollection(String collection) {
        this.collection = collection;
    }

    public int getPieceCount() {
        return pieceCount;
    }
    public void setPieceCount(int pieceCount) {
        this.pieceCount = pieceCount;
    }

    public Integer getYear() {
        return year;
    }
    public void setYear(Integer year) {
        this.year = year;
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }
    public void setUserImageUrl(String imageUrl) {
        this.userImageUrl = imageUrl;
    }

    public Set<UserLegoSet> getUsers() {
        return users;
    }
    public void setUsers(Set<UserLegoSet> users) {
        this.users = users;
    }

    public boolean hasImage() {
        return userImageUrl != null && !userImageUrl.trim().isEmpty();
    }
}
