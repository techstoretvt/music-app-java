package com.example.musicapp.modal.anhxajson;

public class GetCaSiByID {
    private int errCode, status;
    private String errMessage;
    private Casi data;

    public GetCaSiByID(int errCode, int status, String errMessage, Casi data) {
        this.errCode = errCode;
        this.status = status;
        this.errMessage = errMessage;
        this.data = data;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }

    public Casi getData() {
        return data;
    }

    public void setData(Casi data) {
        this.data = data;
    }
}
