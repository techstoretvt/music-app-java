package com.example.musicapp.modal.body;

public class BodyLogin {
    private String email;
    private String pass;

    public BodyLogin() {
        this.email = "";
        this.pass = "";
    }

    public BodyLogin(String email, String pass) {
        this.email = email;
        this.pass = pass;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
