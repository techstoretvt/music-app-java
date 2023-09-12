package com.example.musicapp.modal.anhxajson;

public class Casi {
    private String id;
    private String tenCaSi;
    private String moTa;
    private String anh;

    public Casi(String id, String tenCaSi, String moTa, String anh) {
        this.id = id;
        this.tenCaSi = tenCaSi;
        this.moTa = moTa;
        this.anh = anh;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTenCaSi() {
        return tenCaSi;
    }

    public void setTenCaSi(String tenCaSi) {
        this.tenCaSi = tenCaSi;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getAnh() {
        return anh;
    }

    public void setAnh(String anh) {
        this.anh = anh;
    }
}
