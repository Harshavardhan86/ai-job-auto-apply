package com.harsha.autoapply.dto;

public class UploadResponse {

    private String message;
    private String fileName;

    public UploadResponse() {
    
    }

    public UploadResponse(String message, String fileName) {
        this.message = message;
        this.fileName = fileName;
    }

    public String getMessage() {
        return message;
    }

    public String getFileName() {
        return fileName;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}