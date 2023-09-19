package com.example.musicapp.modal.anhxajson;

public class GetDSPhatById {
    private int errCode, status;
    private String errMessage;
    private DanhSachPhat data;

    public GetDSPhatById(int errCode, int status, String errMessage, DanhSachPhat data) {
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

    public DanhSachPhat getData() {
        return data;
    }

    public void setData(DanhSachPhat data) {
        this.data = data;
    }
}
