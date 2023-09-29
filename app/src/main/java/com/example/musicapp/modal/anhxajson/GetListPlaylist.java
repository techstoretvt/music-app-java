package com.example.musicapp.modal.anhxajson;

import java.util.ArrayList;

public class GetListPlaylist {
    private int errCode, status;

    private String errMessage;
    private ArrayList<DanhSachPhat> data;

    public GetListPlaylist(int errCode, int status, String errMessage, ArrayList<DanhSachPhat> data) {
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

    public ArrayList<DanhSachPhat> getData() {
        return data;
    }

    public void setData(ArrayList<DanhSachPhat> data) {
        this.data = data;
    }
}
