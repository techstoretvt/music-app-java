package com.example.musicapp.modal.anhxajson;

public class DS_Noi_Bat {
    private String urlAnh, tenDS;

    public DS_Noi_Bat(String urlAnh, String tenDS) {
        this.urlAnh = urlAnh;
        this.tenDS = tenDS;
    }

    public String getUrlAnh() {
        return urlAnh;
    }

    public void setUrlAnh(String urlAnh) {
        this.urlAnh = urlAnh;
    }

    public String getTenDS() {
        return tenDS;
    }

    public void setTenDS(String tenDS) {
        this.tenDS = tenDS;
    }
}
