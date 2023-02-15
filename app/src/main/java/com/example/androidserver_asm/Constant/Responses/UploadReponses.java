package com.example.androidserver_asm.Constant.Responses;

public class UploadReponses {
    private Boolean error;
    private String message, path;

    public UploadReponses() {
    }

    public UploadReponses(Boolean error, String message, String path) {
        this.error = error;
        this.message = message;
        this.path = path;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
