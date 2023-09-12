package com.example.musicapp.modal.anhxajson;

import java.util.ArrayList;

public class GetListBaiHat {
    private int errCode;

    private String errMessage;
    private ArrayList<BaiHat> data;

    public GetListBaiHat(int errCode, ArrayList<BaiHat> data) {
        this.errCode = errCode;
        this.data = data;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public ArrayList<BaiHat> getData() {
        return data;
    }

    public void setData(ArrayList<BaiHat> data) {
        this.data = data;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }
}
