package com.example.musicapp.modal.anhxajson;

public class ResponseDefault {
    private int errCode;
    private String errMessage;

    public ResponseDefault(int errCode, String errMessage) {
        this.errCode = errCode;
        this.errMessage = errMessage;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }
}
