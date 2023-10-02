package com.example.musicapp.modal.anhxajson;

public class AddCommentCon {
    private int errCode, status;
    private String errMessage;
    private CommentBaiHatCon data;

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

    public CommentBaiHatCon getData() {
        return data;
    }

    public void setData(CommentBaiHatCon data) {
        this.data = data;
    }
}
