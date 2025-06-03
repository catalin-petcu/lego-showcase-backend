package com.legohub.dto.response;

import com.legohub.model.LegoSet;

public class ImageUploadReponse {
    private String message;
    private String imageUrl;
    private LegoSet legoSet;

    public ImageUploadReponse(String message, String imageUrl, LegoSet legoSet) {
        this.message = message;
        this.imageUrl = imageUrl;
        this.legoSet = legoSet;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public LegoSet getLegoSet() {
        return legoSet;
    }
    public void setLegoSet(LegoSet legoSet) {
        this.legoSet = legoSet;
    }
}
