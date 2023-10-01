package com.example.musicapp.modal.anhxajson;

public class AddCommentParent {
    private int errCode, status;
    private String errMessage;
    private CommentBaiHat data;

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

    public CommentBaiHat getData() {
        return data;
    }

    public void setData(CommentBaiHat data) {
        this.data = data;
    }
}
