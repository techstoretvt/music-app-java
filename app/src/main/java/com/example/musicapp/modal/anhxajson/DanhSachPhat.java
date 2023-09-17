package com.example.musicapp.modal.anhxajson;

import java.io.Serializable;

public class DanhSachPhat implements Serializable {
    private String id;
    private String tenDanhSach;
    private String idUser;

    public DanhSachPhat(String id, String tenDanhSach, String idUser) {
        this.id = id;
        this.tenDanhSach = tenDanhSach;
        this.idUser = idUser;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTenDanhSach() {
        return tenDanhSach;
    }

    public void setTenDanhSach(String tenDanhSach) {
        this.tenDanhSach = tenDanhSach;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }
}
