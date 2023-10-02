package com.example.musicapp.modal.anhxajson;

import java.io.Serializable;
import java.util.ArrayList;

public class DanhSachPhat implements Serializable {
    private String id;
    private String tenDanhSach;
    private String idUser, anhDanhSach;

    private ArrayList<ChiTietDanhSachPhat> chiTietDanhSachPhats;

    public DanhSachPhat(String id, String tenDanhSach, String idUser, ArrayList<ChiTietDanhSachPhat> chiTietDanhSachPhats) {
        this.id = id;
        this.tenDanhSach = tenDanhSach;
        this.idUser = idUser;
        this.chiTietDanhSachPhats = chiTietDanhSachPhats;
    }

    public String getAnhDanhSach() {
        return anhDanhSach;
    }

    public void setAnhDanhSach(String anhDanhSach) {
        this.anhDanhSach = anhDanhSach;
    }

    public ArrayList<ChiTietDanhSachPhat> getChiTietDanhSachPhats() {
        return chiTietDanhSachPhats;
    }

    public void setChiTietDanhSachPhats(ArrayList<ChiTietDanhSachPhat> chiTietDanhSachPhats) {
        this.chiTietDanhSachPhats = chiTietDanhSachPhats;
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
