package com.example.musicapp.modal.anhxajson;

import java.util.ArrayList;

class TimKiemMV_item {
    private BaiHat item;

    public BaiHat getItem() {
        return item;
    }

    public void setItem(BaiHat item) {
        this.item = item;
    }
}

public class TimKiemMV {
    private int errCode, status;
    private String errMessage;
    private ArrayList<TimKiemMV_item> data;

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

    public ArrayList<TimKiemMV_item> getData() {
        return data;
    }

    public BaiHat getDataIndex(int index) {
        return data.get(index).getItem();
    }

    public void setData(ArrayList<TimKiemMV_item> data) {
        this.data = data;
    }
}
