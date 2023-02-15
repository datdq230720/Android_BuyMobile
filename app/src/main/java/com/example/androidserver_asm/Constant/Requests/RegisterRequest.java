package com.example.androidserver_asm.Constant.Requests;

public class RegisterRequest {
    private String email;
    private String password;

    public RegisterRequest() {
    }

    public RegisterRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmial() {
        return email;
    }

    public void setEmial(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
