package com.example.musicapp.modal.anhxajson;

import java.util.ArrayList;

public class GetListCommentById {
    private int errCode, status;
    private String errMessage;
    private ArrayList<CommentBaiHat> data;

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

    public ArrayList<CommentBaiHat> getData() {
        return data;
    }

    public void setData(ArrayList<CommentBaiHat> data) {
        this.data = data;
    }
}
