package com.example.musicapp.modal.anhxajson;

import java.util.ArrayList;

public class DanhSachCaSi {
    private int errCode, status;
    private String errMessage;
    private ArrayList<Casi> data;

    public DanhSachCaSi(int errCode, int status, String errMessage, ArrayList<Casi> data) {
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

    public ArrayList<Casi> getData() {
        return data;
    }

    public void setData(ArrayList<Casi> data) {
        this.data = data;
    }
}
