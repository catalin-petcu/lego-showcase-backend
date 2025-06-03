package com.legohub.dto.response;

public class UploadResponse {
    private String message;
    private String filename;
    private String imageUrl;

    public UploadResponse(String message, String filename, String imageUrl) {
        this.message = message;
        this.filename = filename;
        this.imageUrl = imageUrl;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public String getFilename() {
        return filename;
    }
    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
