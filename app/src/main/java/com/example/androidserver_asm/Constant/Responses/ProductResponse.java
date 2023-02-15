package com.example.androidserver_asm.Constant.Responses;

public class ProductResponse {
    private Boolean status;
    private Boolean result;

    public ProductResponse() {
    }

    public ProductResponse(Boolean status, Boolean result) {
        this.status = status;
        this.result = result;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }
}
