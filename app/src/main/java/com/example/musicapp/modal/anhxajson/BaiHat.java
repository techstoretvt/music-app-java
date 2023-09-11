package com.example.musicapp.modal.anhxajson;

public class BaiHat {
    private String id;
    private String tenBaiHat;
    private String tenCaSi;

    public BaiHat(String id, String tenBaiHat, String tenCaSi) {
        this.id = id;
        this.tenBaiHat = tenBaiHat;
        this.tenCaSi = tenCaSi;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTenBaiHat() {
        return tenBaiHat;
    }

    public void setTenBaiHat(String tenBaiHat) {
        this.tenBaiHat = tenBaiHat;
    }

    public String getTenCaSi() {
        return tenCaSi;
    }

    public void setTenCaSi(String tenCaSi) {
        this.tenCaSi = tenCaSi;
    }
}
