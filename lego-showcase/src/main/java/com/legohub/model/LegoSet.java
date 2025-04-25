package com.legohub.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class LegoSet {
    @Id
    private Long setId;

    private String name;
    private String collection;
    private int pieceCount;

    public LegoSet() {}

    public LegoSet(Long setId, String name, String collection, int pieceCount) {
        this.setId = setId;
        this.name = name;
        this.collection = collection;
        this.pieceCount = pieceCount;
    }

    public Long getSetId() {
        return setId;
    }

    public void setSetId(Long setId) {
        this.setId = setId;
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
}
