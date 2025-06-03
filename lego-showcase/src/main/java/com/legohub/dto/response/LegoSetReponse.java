package com.legohub.dto.response;

import com.legohub.model.LegoSet;

public class LegoSetReponse {
    private String message;
    private LegoSet legoSet;

    public LegoSetReponse(String message, LegoSet legoSet) {
        this.message = message;
        this.legoSet = legoSet;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public LegoSet getLegoSet() {
        return legoSet;
    }
    public void setLegoSet(LegoSet legoSet) {
        this.legoSet = legoSet;
    }
}
