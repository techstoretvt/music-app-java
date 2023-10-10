package com.example.musicapp.modal.anhxajson;

import java.util.ArrayList;

public class GetLoiBaiHat {
    private int errCode, status;
    private String errMessage;
    private ArrayList<LoiBaiHat> data;

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

    public ArrayList<LoiBaiHat> getData() {
        return data;
    }

    public void setData(ArrayList<LoiBaiHat> data) {
        this.data = data;
    }
}
