package com.example.musicapp.modal.anhxajson;

public class YeuThichBaiHat {
    private String idBaiHat, idUser;
    private BaiHat baihat;

    public YeuThichBaiHat(String idBaiHat, String idUser, BaiHat baihat) {
        this.idBaiHat = idBaiHat;
        this.idUser = idUser;
        this.baihat = baihat;
    }

    public String getIdBaiHat() {
        return idBaiHat;
    }

    public void setIdBaiHat(String idBaiHat) {
        this.idBaiHat = idBaiHat;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public BaiHat getBaihat() {
        return baihat;
    }

    public void setBaihat(BaiHat baihat) {
        this.baihat = baihat;
    }
}
