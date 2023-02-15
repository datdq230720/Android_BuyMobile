package com.example.androidserver_asm.Constant.Requests;

public class UploadRequests {
    private String base64;

    public UploadRequests() {
    }

    public UploadRequests(String base64) {
        this.base64 = base64;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }
}
