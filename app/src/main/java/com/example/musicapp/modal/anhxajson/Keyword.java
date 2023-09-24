package com.example.musicapp.modal.anhxajson;

import java.util.ArrayList;

class Item_keyword {
    private String keyword;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}

public class Keyword {
    private int errCode, status;
    private String errMessage;
    private ArrayList<Item_keyword> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public ArrayList<Item_keyword> getData() {
        return data;
    }

    public void setData(ArrayList<Item_keyword> data) {
        this.data = data;
    }

    public String getKeywordByIndex(int index) {
        return data.get(index).getKeyword();
    }
}
