package com.example.musicapp.modal.anhxajson;

class Token {
    private String accessToken;
    private String refreshToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}

public class Login {
    private int errCode;
    private String errMessage;
    private Token data;

    public Login(int errCode, String errMessage, Token data) {
        this.errCode = errCode;
        this.errMessage = errMessage;
        this.data = data;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public Token getData() {
        return data;
    }

    public void setData(Token data) {
        this.data = data;
    }

    public String getAccessToken() {
        return data.getAccessToken();
    }

    public String getRefreshToken() {
        return data.getRefreshToken();
    }
}
