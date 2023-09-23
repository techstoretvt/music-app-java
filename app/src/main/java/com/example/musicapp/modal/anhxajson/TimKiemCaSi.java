package com.example.musicapp.modal.anhxajson;

import java.util.ArrayList;

class ItemCS {
    private Casi item;

    public ItemCS(Casi item) {
        this.item = item;
    }

    public Casi getItem() {
        return item;
    }

    public void setItem(Casi item) {
        this.item = item;
    }
}

public class TimKiemCaSi {
    private int errCode, status;
    private String errMessage;
    private ArrayList<ItemCS> data;

    public TimKiemCaSi(int errCode, int status, String errMessage, ArrayList<ItemCS> data) {
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

    public ArrayList<ItemCS> getData() {
        return data;
    }

    public void setData(ArrayList<ItemCS> data) {
        this.data = data;
    }

    public Casi getCaSiIndex(int index) {
        return data.get(index).getItem();
    }
}
