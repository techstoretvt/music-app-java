package com.example.musicapp.modal.body;

public class BodyChangePass {
    private String email;
    private String keyVerify;
    private String pass;

    public BodyChangePass(String email, String keyVerify, String pass) {
        this.email = email;
        this.keyVerify = keyVerify;
        this.pass = pass;
    }
}
